package clausEnterprises.crm.controller;

import clausEnterprises.crm.dto.MailDto;
import clausEnterprises.crm.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final EmailService emailService;

    @RequestMapping(value = "/letter")
    public String sendLetterToTheCompany(@ModelAttribute("letter") @Valid MailDto letter, BindingResult bindingResult,
                                         Model model)
            throws MessagingException, JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return "letter";
        }
        return emailService.sendEmailToCompany(letter, model);
    }
}
