package clausEnterprises.crm.controller;

import clausEnterprises.crm.dto.CreationDto;
import clausEnterprises.crm.dto.FinishOrderDto;
import clausEnterprises.crm.dto.OrderDto;
import clausEnterprises.crm.model.Order;
import clausEnterprises.crm.model.Warehouse;
import clausEnterprises.crm.service.CourierService;
import clausEnterprises.crm.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/crm/courierService")
@AllArgsConstructor
public class CourierController {
    private final CourierService courierService;
    private final WarehouseService warehouseService;

    @RequestMapping(value = "/getAvailableGifts")
    public String getGoods(@ModelAttribute("availableGifts") CreationDto availableGifts, Model model) {
        List<Warehouse> gifts = courierService.getAllGoodOnTheWarehouse();
        availableGifts.setDtos(gifts);
        model.addAttribute("availableGifts", availableGifts);
        return "availableGifts";
    }

    @RequestMapping(value = "/getAvailableOrders")
    public String getAvailableOrders(@ModelAttribute("availableOrders") CreationDto availableOrders, Model model) {
        List<Order> orders = courierService.getAvailableOrders();
        availableOrders.setDtos(orders);
        model.addAttribute("availableOrders", availableOrders);
        return "availableOrders";
    }

    @RequestMapping(value = "/acceptOrder")
    public String acceptAvailableOrder(@ModelAttribute("acceptOrder") @Valid OrderDto orderDto,
                                       BindingResult bindingResult,
                                       Model model) {
        model.addAttribute("orderIds", new ArrayList<>(courierService.getAllOrderIds()));
        model.addAttribute("giftIds", new ArrayList<>(warehouseService.getAllGiftIds()));
        model.addAttribute("courierIds", new ArrayList<>(courierService.getAllCourierIds()));
        if (bindingResult.hasErrors()) {
            return "acceptOrder";
        }
        String result = courierService.acceptOrder(orderDto);
        model.addAttribute("acceptOrder", orderDto);
        return result;
    }

    @RequestMapping(value = "/finishOrder")
    public String finishOrder(@ModelAttribute("order") @Valid FinishOrderDto order,
                              BindingResult bindingResult,
                              Model model) {
        model.addAttribute("orderIds", new ArrayList<>(courierService.getAllOrderIds()));
        model.addAttribute("courierIds", new ArrayList<>(courierService.getAllCourierIds()));
        if (bindingResult.hasErrors()) {
            return "finishOrder";
        }
        model.addAttribute("order", order);
        return courierService.finishTheOrder(order);
    }
}
