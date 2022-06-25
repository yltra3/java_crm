package clausEnterprises.crm.service;

import clausEnterprises.crm.AbstractIntegrationTest;
import clausEnterprises.crm.consts.enums.OrderStatus;
import clausEnterprises.crm.dto.FinishOrderDto;
import clausEnterprises.crm.dto.OrderDto;
import clausEnterprises.crm.model.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static clausEnterprises.crm.TestUtils.printMyName;

@RunWith(SpringRunner.class)
@Sql({"/courierTest.sql"})
public class CourierServiceTest extends AbstractIntegrationTest {
    @Autowired
    private CourierService courierService;

    @Test
    public void testAcceptOrderWithExistingData() {
        printMyName();
        OrderDto orderDto = OrderDto.builder()
                .courierId(1L)
                .orderId(1L)
                .giftId(1L)
                .build();
        String response = courierService.acceptOrder(orderDto);
        Order order = courierService.getOneOrder(orderDto.getOrderId());
        Assert.assertNotNull(order);
        Assert.assertEquals(false, order.getIsActual());
        Assert.assertEquals("acceptOrderSubmit", response);
    }

    @Test
    public void testAcceptOrderWithNonExistingData() {
        printMyName();
        OrderDto order = OrderDto.builder()
                .courierId(5L)
                .orderId(1L)
                .giftId(1L)
                .build();
        String response = courierService.acceptOrder(order);
        Assert.assertEquals("acceptOrderError", response);
    }

    @Test
    public void testSuccessfullyFinishingAnOrder() {
        FinishOrderDto input = FinishOrderDto.builder()
                .courierId(1L)
                .orderId(1L)
                .orderStatus(OrderStatus.DELIVERED)
                .build();
        String response = courierService.finishTheOrder(input);
        Order order = courierService.getOneOrder(input.getOrderId());
        Assert.assertNotNull(order);
        Assert.assertEquals(java.util.Optional.of(1L).get(), input.getCourierId());
        Assert.assertEquals("finishOrderSubmit", response);
    }
}
