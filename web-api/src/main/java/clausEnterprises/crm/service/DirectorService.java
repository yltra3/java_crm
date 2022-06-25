package clausEnterprises.crm.service;

import clausEnterprises.crm.config.SheetsServiceConfig;
import clausEnterprises.crm.consts.enums.Behaviour;
import clausEnterprises.crm.consts.enums.EmailStatus;
import clausEnterprises.crm.consts.enums.RejectionReason;
import clausEnterprises.crm.dto.CreationDto;
import clausEnterprises.crm.dto.ParentResponseStatus;
import clausEnterprises.crm.dto.ReportResultDto;
import clausEnterprises.crm.model.Courier;
import clausEnterprises.crm.model.ParentResponse;
import clausEnterprises.crm.repository.ReportRepository;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static clausEnterprises.crm.consts.MailCredentials.FIRST_RESPONSE_SPREADSHEET_ID;
import static clausEnterprises.crm.consts.MailCredentials.SECOND_RESPONSE_SPREADSHEET_ID;

@Service
@Slf4j
@AllArgsConstructor
public class DirectorService {
    private final ReportRepository reportRepository;
    private final ClientService clientService;
    private final EmailService emailService;
    private final ParentResponseService parentResponseService;

    @Transactional(readOnly = true)
    public ReportResultDto getReportForASingleUser(CreationDto dto) {
        Courier courier = reportRepository.getCourierStatsForCertainCourier(dto.getId());
        if (courier == null) {
            return ReportResultDto.builder().build();
        }
        return ReportResultDto.builder()
                .firstname(courier.getCourier().getFirstname())
                .lastname(courier.getCourier().getLastname())
                .deliveriesFailed(courier.getTimesFailed())
                .deliveriesDone(courier.getTimesDelivered())
                .efficiency(countCourierEfficiency(courier.getTimesDelivered(), courier.getTimesFailed()))
                .build();
    }

    @Transactional(readOnly = true)
    public List<ReportResultDto> getReportForAllCouriers() {
        List<Courier> courierStats = reportRepository.findAll();
        if (courierStats.size() == 0) {
            return Collections.emptyList();
        }
        return courierStats
                .stream()
                .map(stat -> ReportResultDto.builder()
                        .firstname(stat.getCourier().getFirstname())
                        .lastname(stat.getCourier().getLastname())
                        .deliveriesFailed(stat.getTimesFailed())
                        .deliveriesDone(stat.getTimesDelivered())
                        .efficiency(countCourierEfficiency(stat.getTimesDelivered(), stat.getTimesFailed()))
                        .build())
                .collect(Collectors.toList());
    }


    public List<ParentResponseStatus> checkTheSecondRequestToTheParents() throws GeneralSecurityException, IOException {
        List<ParentResponseStatus> parentResponses = new ArrayList<>();
        Sheets sheets = SheetsServiceConfig.getSheetsService();
        ValueRange readResult = sheets.spreadsheets().values()
                .get(SECOND_RESPONSE_SPREADSHEET_ID, "B2:D65535")
                .execute();
        List<List<Object>> values = readResult.getValues();
        if (values != null) {
            values.forEach(value -> {
                if (value.size() > 0) {
                    String parentEmail = value.get(0).toString();
                    String response = value.get(1).toString();
                    String childEmail = value.get(2).toString();
                    ParentResponse parentResponse = parentResponseService.getResponseByEmails(childEmail, parentEmail);
                    if (response.equals("GOOD")) {
                        parentResponse.setBehaviour(Behaviour.GOOD);
                        parentResponseService.updateResponseForASingleChild(parentResponse);
                        parentResponses.add(ParentResponseStatus.builder()
                                .clientEmail(childEmail)
                                .parentEmail(parentEmail)
                                .emailStatus(EmailStatus.ACCEPTED)
                                .build());
                    } else if (response.equals("BAD")) {
                        clientService.deleteClientByEmails(childEmail, parentEmail);
                        try {
                            emailService.sendRejectionToClient(childEmail, RejectionReason.BAD_BEHAVIOUR);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                        parentResponses.add(ParentResponseStatus.builder()
                                .clientEmail(childEmail)
                                .parentEmail(parentEmail)
                                .emailStatus(EmailStatus.REJECTED)
                                .build());
                    } else {
                        clientService.deleteClientByEmails(childEmail, parentEmail);
                        try {
                            emailService.sendRejectionToClient(childEmail, RejectionReason.BAD_BEHAVIOUR);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                        parentResponses.add(ParentResponseStatus.builder()
                                .clientEmail(childEmail)
                                .parentEmail(parentEmail)
                                .emailStatus(EmailStatus.REJECTED)
                                .build());
                    }
                }
            });
            sheets.spreadsheets().values().clear(FIRST_RESPONSE_SPREADSHEET_ID, "A1:D65535",
                    new ClearValuesRequest()).execute();
        }
        return parentResponses;
    }

    private BigDecimal countCourierEfficiency(Long timesDelivered, Long timesFailedToDeliver) {
        if (timesDelivered == 0) {
            return BigDecimal.ZERO;
        } else if (timesDelivered > timesFailedToDeliver) {
            return BigDecimal.valueOf((timesFailedToDeliver.doubleValue() / timesDelivered.doubleValue()) * 100.0);
        } else return BigDecimal.valueOf((timesDelivered.doubleValue() / timesFailedToDeliver.doubleValue()) * 100);
    }
}
