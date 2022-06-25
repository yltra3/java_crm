package clausEnterprises.crm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
        viewControllerRegistry.addViewController("/").setViewName("home");
        viewControllerRegistry.addViewController("/sign-in").setViewName("sign-in");
        viewControllerRegistry.addViewController("/access-denied").setViewName("access-denied");
        viewControllerRegistry.addViewController("/error").setViewName("error");
        viewControllerRegistry.addViewController("/letter").setViewName("letter");
        viewControllerRegistry.addViewController("/emailCheck").setViewName("emailCheck");
        viewControllerRegistry.addViewController("/parentResponseCheck").setViewName("parentResponseCheck");
        viewControllerRegistry.addViewController("/createDeliveryOrders").setViewName("deliveryOrder");
        viewControllerRegistry.addViewController("/getCourierReport").setViewName("courierReport");
        viewControllerRegistry.addViewController("/getGlobalReport").setViewName("globalReport");
        viewControllerRegistry.addViewController("/checkParentResponsesSecondTime").setViewName("unclearResponse");
        viewControllerRegistry.addViewController("/getAvailableOrders").setViewName("availableOrders");
        viewControllerRegistry.addViewController("/getAvailableGifts").setViewName("availableGifts");
        viewControllerRegistry.addViewController("/acceptOrder").setViewName("acceptOrder");
        viewControllerRegistry.addViewController("/finishOrder").setViewName("finishOrder");
        viewControllerRegistry.addViewController("/checkUnclearResponse").setViewName("responseForUnknown");
    }
}
