package clausEnterprises.crm.service;

import clausEnterprises.crm.AbstractIntegrationTest;
import clausEnterprises.crm.model.Warehouse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static clausEnterprises.crm.TestUtils.printMyName;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Sql({"/warehouse.sql"})
public class WarehouseServiceTest extends AbstractIntegrationTest {
    @Autowired
    private WarehouseService warehouseService;

    @Test
    public void getExistingGiftByIdTest() {
        printMyName();
        Warehouse result = warehouseService.getGiftById(1L);
        assertNotNull(result);
        assertEquals(Warehouse.builder()
                        .id(1L)
                        .description("Gift 843491731")
                        .is_available(true)
                        .build(),
                result);
    }

    @Test
    public void getNonExistingGiftByIdTest() {
        printMyName();
        Warehouse result = warehouseService.getGiftById(700L);
        assertNull(result);
    }

    @Test
    public void getAllAvailableGiftsTest() {
        printMyName();
        List<Long> gifts = warehouseService.getAllGiftIds();
        assertEquals(4, gifts.size());
        assertEquals(Arrays.asList(1L, 8L, 9L, 10L), gifts);
    }
}
