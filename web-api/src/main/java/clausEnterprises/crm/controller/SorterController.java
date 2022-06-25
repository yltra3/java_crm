package clausEnterprises.crm.controller;

import clausEnterprises.crm.consts.MailCredentials;
import clausEnterprises.crm.dto.CreationDto;
import clausEnterprises.crm.dto.ParentResponseStatus;
import clausEnterprises.crm.dto.ProcessedClientEmailDto;
import clausEnterprises.crm.dto.ProcessedParentResponse;
import clausEnterprises.crm.service.SorterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@RequestMapping("crm/sorterService")
public class SorterController {
    private final SorterService sorterService;
    private final String username;
    private final String password;

    public SorterController(SorterService sorterService,
                            @Value("${spring.mail.username}") String username,
                            @Value("${spring.mail.password}") String password) {
        this.sorterService = sorterService;
        this.username = username;
        this.password = password;
    }

    @RequestMapping(value = "/emailCheck")
    public String checkEmail(@ModelAttribute("emailCheck") CreationDto emailCheck, Model model)
            throws MessagingException, IOException {
        List<ProcessedClientEmailDto> processedClientEmailDtos = sorterService.checkClientEmails(
                MailCredentials.HOST,
                MailCredentials.MAIL_STORE_TYPE,
                username,
                password);
        emailCheck.setDtos(processedClientEmailDtos);
        model.addAttribute("emailCheck", emailCheck);
        return "emailCheck";
    }

    @RequestMapping(value = "/parentResponseCheck")
    public String checkRequests(@ModelAttribute("parentResponseCheck") CreationDto parentResponseCheck,
                                Model model) throws IOException, GeneralSecurityException {
        List<ProcessedParentResponse> processedParentResponses = sorterService.checkParentResponse();
        parentResponseCheck.setDtos(processedParentResponses);
        model.addAttribute("parentResponseCheck", parentResponseCheck);
        return "parentResponseCheck";
    }

    @RequestMapping(value = "/createDeliveryOrders")
    public String createDeliveryOrders(@ModelAttribute("deliveryOrder") CreationDto deliveryOrder,
                                       Model model) {
        List<ParentResponseStatus> processedParentResponses = sorterService.createDeliveryOrders();
        deliveryOrder.setDtos(processedParentResponses);
        model.addAttribute("deliveryOrder", deliveryOrder);
        return "deliveryOrder";
    }
}
