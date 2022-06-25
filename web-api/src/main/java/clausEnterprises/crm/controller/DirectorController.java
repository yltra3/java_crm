package clausEnterprises.crm.controller;


import clausEnterprises.crm.dto.CreationDto;
import clausEnterprises.crm.dto.ParentResponseStatus;
import clausEnterprises.crm.dto.ProcessedParentResponse;
import clausEnterprises.crm.service.CourierService;
import clausEnterprises.crm.service.DirectorService;
import clausEnterprises.crm.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/crm/directorService")
@AllArgsConstructor
public class DirectorController {
    private final DirectorService directorService;
    private final CourierService courierService;
    private final EmailService emailService;

    @RequestMapping(value = "/createCourierReport")
    public String getReportForASingleCourier(@ModelAttribute("courierReport") @Valid CreationDto courierReport,
                                             BindingResult bindingResult,
                                             Model model) {
        model.addAttribute("courierIds", new ArrayList<>(courierService.getAllCourierIds()));
        if (bindingResult.hasErrors()) {
            return "courierReport";
        }
        courierReport.setDtos(Collections.singletonList(directorService.getReportForASingleUser(courierReport)));
        model.addAttribute("courierReport", courierReport);
        return "new-courierReport";
    }

    @RequestMapping(value = "/createGlobalReport")
    public String getGlobalReport(@ModelAttribute("globalReport") CreationDto globalReport,
                                  Model model) {
        globalReport.setDtos(directorService.getReportForAllCouriers());
        model.addAttribute("globalReport", globalReport);
        return "globalReport";
    }

    @RequestMapping(value = "/checkUnclearResponse")
    public String checkUnclearResponse(@ModelAttribute("responseForUnknown") CreationDto responseForUnknown,
                                       Model model) {
        List<ProcessedParentResponse> processedParentResponses = emailService.findEmailsForUnclearResponses();
        responseForUnknown.setDtos(processedParentResponses);
        model.addAttribute("responseForUnknown", responseForUnknown);
        return "responseForUnknown";
    }

    @RequestMapping(value = "/checkParentResponsesSecondTime")
    public String checkParentResponsesSecondTime(@ModelAttribute("unclearResponse") CreationDto unclearResponse, Model model)
            throws GeneralSecurityException, IOException {
        List<ParentResponseStatus> processedParentResponses = directorService.checkTheSecondRequestToTheParents();
        unclearResponse.setDtos(processedParentResponses);
        model.addAttribute("unclearResponse", unclearResponse);
        return "unclearResponse";
    }

}
