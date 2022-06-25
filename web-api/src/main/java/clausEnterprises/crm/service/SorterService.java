package clausEnterprises.crm.service;

import clausEnterprises.crm.config.SheetsServiceConfig;
import clausEnterprises.crm.consts.enums.Behaviour;
import clausEnterprises.crm.consts.enums.EmailStatus;
import clausEnterprises.crm.consts.enums.RejectionReason;
import clausEnterprises.crm.dto.MailDto;
import clausEnterprises.crm.dto.ParentResponseStatus;
import clausEnterprises.crm.dto.ProcessedClientEmailDto;
import clausEnterprises.crm.dto.ProcessedParentResponse;
import clausEnterprises.crm.model.Client;
import clausEnterprises.crm.model.Order;
import clausEnterprises.crm.model.ParentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static clausEnterprises.crm.consts.MailCredentials.FIRST_RESPONSE_SPREADSHEET_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SorterService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final EmailService emailService;
    private final CourierService courierService;
    private final ParentResponseService parentResponseService;
    private final ClientService clientService;

    public List<ProcessedClientEmailDto> checkClientEmails(String host, String storeType, String user, String password)
            throws MessagingException, IOException {
        List<ProcessedClientEmailDto> processedClientEmailDtos = new ArrayList<>();
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", storeType);
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore(storeType);
        store.connect(host, user, password);
        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_WRITE);

        Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        for (Message message : messages) {
            if (!Objects.equals(message.getSubject(), "Gifts for behaviour")) {
                continue;
            }
            String email = getTextFromMessage(message);
            MailDto childrenMessage = mapper.readValue(email, MailDto.class);

            try {
                if (childrenMessage.getParentEmail().equals(childrenMessage.getEmail())) {
                    emailService.sendRejectionToClient(childrenMessage.getEmail(), RejectionReason.SAME_EMAIL);
                    processedClientEmailDtos.add(ProcessedClientEmailDto.builder()
                            .clientEmail(childrenMessage.getEmail())
                            .parentEmail(childrenMessage.getParentEmail())
                            .status(EmailStatus.REJECTED)
                            .build());

                } else if (clientService.getAlreadyExistingClient(childrenMessage.getEmail()) != null) {
                    emailService.sendRejectionToClient(childrenMessage.getEmail(), RejectionReason.ALREADY_REGISTERED);
                    processedClientEmailDtos.add(ProcessedClientEmailDto.builder()
                            .clientEmail(childrenMessage.getEmail())
                            .parentEmail(childrenMessage.getParentEmail())
                            .status(EmailStatus.REJECTED)
                            .build());
                } else if (emailService.isAddressValid(childrenMessage.getParentEmail())) {
                    responseToParents(childrenMessage);
                    processedClientEmailDtos.add(ProcessedClientEmailDto.builder()
                            .clientEmail(childrenMessage.getEmail())
                            .parentEmail(childrenMessage.getParentEmail())
                            .status(EmailStatus.ACCEPTED)
                            .build());
                } else {
                    emailService.sendRejectionToClient(childrenMessage.getEmail(), RejectionReason.WRONG_EMAIL);
                    processedClientEmailDtos.add(ProcessedClientEmailDto.builder()
                            .clientEmail(childrenMessage.getEmail())
                            .parentEmail(childrenMessage.getParentEmail())
                            .status(EmailStatus.REJECTED)
                            .build());
                }
            } catch (Exception exception) {
                log.error("Error happened while checking email - {}", exception.getMessage());
                emailService.sendRejectionToClient(childrenMessage.getEmail(), RejectionReason.WRONG_EMAIL);
                processedClientEmailDtos.add(ProcessedClientEmailDto.builder()
                        .clientEmail(childrenMessage.getEmail())
                        .parentEmail(childrenMessage.getParentEmail())
                        .status(EmailStatus.REJECTED)
                        .build());
            }

            message.setFlag(Flags.Flag.SEEN, true);
        }
        emailFolder.close(false);
        store.close();
        return processedClientEmailDtos;
    }

    public List<ProcessedParentResponse> checkParentResponse() throws GeneralSecurityException, IOException {
        List<ProcessedParentResponse> processedParentResponses = new ArrayList<>();
        Sheets sheets = SheetsServiceConfig.getSheetsService();
        ValueRange readResult = sheets.spreadsheets().values()
                .get(FIRST_RESPONSE_SPREADSHEET_ID, "B2:D65535")
                .execute();
        List<List<Object>> values = readResult.getValues();
        if (values != null) {
            values.forEach(value -> {
                if (value.size() > 0) {
                    String parentEmail = value.get(0).toString();
                    String response = value.get(1).toString();
                    String childEmail = value.get(2).toString();
                    ParentResponse parentResponse = parentResponseService.getResponseByEmails(childEmail, parentEmail);
                    if (response.equals("YES")) {
                        parentResponse.setBehaviour(Behaviour.GOOD);
                        parentResponseService.updateResponseForASingleChild(parentResponse);
                        processedParentResponses.add(ProcessedParentResponse.builder()
                                .clientEmail(childEmail)
                                .parentEmail(parentEmail)
                                .behaviour(Behaviour.GOOD)
                                .build());
                    } else if (response.equals("NO")) {
                        parentResponse.setBehaviour(Behaviour.BAD);
                        parentResponseService.updateResponseForASingleChild(parentResponse);
                        processedParentResponses.add(ProcessedParentResponse.builder()
                                .clientEmail(childEmail)
                                .parentEmail(parentEmail)
                                .behaviour(Behaviour.BAD)
                                .build());
                    } else {
                        parentResponse.setBehaviour(Behaviour.UNKNOWN);
                        parentResponseService.updateResponseForASingleChild(parentResponse);
                        processedParentResponses.add(ProcessedParentResponse.builder()
                                .clientEmail(childEmail)
                                .parentEmail(parentEmail)
                                .behaviour(Behaviour.UNKNOWN)
                                .build());
                    }
                }
            });
            sheets.spreadsheets().values().clear(FIRST_RESPONSE_SPREADSHEET_ID, "A1:D65535", new ClearValuesRequest()).execute();
        }
        return processedParentResponses;
    }

    public List<ParentResponseStatus> createDeliveryOrders() {
        List<ParentResponseStatus> processedParentResponses = new ArrayList<>();
        parentResponseService.getAllCheckedResponses().forEach(response ->
        {
            if (response.getBehaviour().equals(Behaviour.GOOD)) {
                courierService.createNewDeliveryOrder(Order.builder()
                        .address(response.getChildren().getAddress())
                        .children(response.getChildren())
                        .dateCreated(LocalDateTime.now())
                        .country(response.getChildren().getCountry())
                        .gift(null)
                        .isActual(true)
                        .courier(null)
                        .build());
                Client clientToUpdate = response.getChildren();
                clientToUpdate.setBehaviour(Behaviour.GOOD);
                clientService.saveClient(clientToUpdate);
                processedParentResponses.add(ParentResponseStatus.builder()
                        .clientEmail(response.getChildren().getEmail())
                        .parentEmail(response.getChildren().getParentEmail())
                        .emailStatus(EmailStatus.ACCEPTED)
                        .build());
                parentResponseService.deleteTheResponse(response);

            } else {
                processedParentResponses.add(ParentResponseStatus.builder()
                        .clientEmail(response.getChildren().getEmail())
                        .parentEmail(response.getChildren().getParentEmail())
                        .emailStatus(EmailStatus.REJECTED)
                        .build());
                clientService.deleteClient(response.getChildren());
            }
        });
        return processedParentResponses;
    }


    private void responseToParents(MailDto mailDto) throws MessagingException {
        emailService.sendLetterToParents(mailDto);
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }
}