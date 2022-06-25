package clausEnterprises.crm.service;

import clausEnterprises.crm.consts.enums.OrderStatus;
import clausEnterprises.crm.dto.FinishOrderDto;
import clausEnterprises.crm.dto.OrderDto;
import clausEnterprises.crm.model.Client;
import clausEnterprises.crm.model.Courier;
import clausEnterprises.crm.model.Order;
import clausEnterprises.crm.model.Warehouse;
import clausEnterprises.crm.repository.CourierRepository;
import clausEnterprises.crm.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierService {
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final ClientService clientService;
    private final WarehouseService warehouseService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNewDeliveryOrder(Order order) {
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Warehouse> getAllGoodOnTheWarehouse() {
        return warehouseService.findAllEntities();
    }

    @Transactional(readOnly = true)
    public List<Order> getAvailableOrders() {
        return orderRepository.getAvailableOrders();
    }
    @Transactional(readOnly = true)
    public Order getOneOrder(Long id) {
        return orderRepository.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Long> getAllCourierIds() {
        return courierRepository.getAllCouriersId();
    }

    @Transactional(readOnly = true)
    public List<Long> getAllOrderIds() {
        return orderRepository.getAllOrdersId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String acceptOrder(OrderDto orderDto) {
        Order chosenOrder = orderRepository.getOrderById(orderDto.getOrderId());
        Warehouse anyGift = warehouseService.getGiftById(orderDto.getGiftId());
        Courier courier = courierRepository.getCourierById(orderDto.getCourierId());
        if (chosenOrder != null && anyGift != null && courier != null) {
            chosenOrder.setIsActual(false);
            chosenOrder.setCourier(courier);
            chosenOrder.setGift(anyGift);
            orderRepository.save(chosenOrder);
            anyGift.setIs_available(false);
            warehouseService.saveGift(anyGift);
            courier.setActiveDeliveries(courier.getActiveDeliveries() + 1);
            courierRepository.save(courier);
            return "acceptOrderSubmit";
        } else {
            return "acceptOrderError";
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String finishTheOrder(FinishOrderDto finishOrderDto) {
        Order chosenOrder = orderRepository.getOrderById(finishOrderDto.getOrderId());
        Courier courier = courierRepository.getCourierById(finishOrderDto.getCourierId());
        if (chosenOrder != null && courier != null) {
            if (finishOrderDto.getOrderStatus().equals(OrderStatus.DELIVERED)) {
                courier.setActiveDeliveries(courier.getActiveDeliveries() - 1);
                courier.setTimesDelivered(courier.getTimesDelivered() + 1);
                courierRepository.save(courier);
                Client client = chosenOrder.getChildren();
                client.setLastTimeDelivered(LocalDateTime.now());
                clientService.saveClient(client);

            } else {
                courier.setActiveDeliveries(courier.getActiveDeliveries() - 1);
                courier.setTimesFailed(courier.getTimesFailed() + 1);
                courierRepository.save(courier);
            }
            return "finishOrderSubmit";
        } else {
            return "acceptOrderError";
        }
    }
}
