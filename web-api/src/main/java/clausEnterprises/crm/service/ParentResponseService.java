package clausEnterprises.crm.service;

import clausEnterprises.crm.consts.MessagesConst;
import clausEnterprises.crm.consts.enums.Behaviour;
import clausEnterprises.crm.model.ParentResponse;
import clausEnterprises.crm.repository.ParentResponseRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@AllArgsConstructor
public class ParentResponseService {
    private final ParentResponseRepository parentResponseRepository;
    private final JavaMailSender mailSender;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveResponse(ParentResponse parentResponse) {
        parentResponseRepository.save(parentResponse);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateResponseForASingleChild(ParentResponse parentResponse) {
        parentResponseRepository.save(parentResponse);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteTheResponse(ParentResponse parentResponse) {
        parentResponseRepository.delete(parentResponse);
    }

    @Transactional(readOnly = true)
    public ParentResponse getResponseByEmails(String email, String parentEmail) {
        return parentResponseRepository.getParentResponseBySingleClient(email, parentEmail);
    }

    @Transactional(readOnly = true)
    public List<ParentResponse> getAllCheckedResponses() {
        return parentResponseRepository.getClearResponses(Behaviour.UNKNOWN);
    }

    @Transactional(readOnly = true)
    public List<ParentResponse> checkParentResponsesSecondTime() {
        List<ParentResponse> parentResponses = parentResponseRepository.getUnclearParentResponses(Behaviour.UNKNOWN);
        parentResponses.forEach(response ->
        {
            try {
                sendSecondEmailToParent(response.getChildren().getParentEmail());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        return parentResponses;
    }

    private void sendSecondEmailToParent(String email) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(email);
        helper.setSubject("Unclear Behaviour");
        helper.setText(MessagesConst.SECOND_TRY_TO_PARENT);
        mailSender.send(message);
    }
}
