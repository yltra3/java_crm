package clausEnterprises.crm.service;

import clausEnterprises.crm.AbstractIntegrationTest;
import clausEnterprises.crm.model.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static clausEnterprises.crm.TestUtils.printMyName;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Sql({"/clientTest.sql"})
public class ClientServiceTest extends AbstractIntegrationTest {
    @Autowired
    private ClientService clientService;

    @Test
    public void testGetExistingClientByEmailWithPreviousDelivery() {
        printMyName();
        String existingEmail = "dantab1@gmail.com";
        Client existingClient = clientService.getAlreadyExistingClient(existingEmail);
        assertNotNull(existingClient);
        assertEquals(existingEmail, existingClient.getEmail());
    }

    @Test
    public void testGetExistingClientByEmailWithoutDeliveries() {
        printMyName();
        String existingEmail = "dantab2@gmail.com";
        Client existingClient = clientService.getAlreadyExistingClient(existingEmail);
        assertNull(existingClient);
    }

    @Test
    public void testDeleteClientIfResponseIsOutdated() {
        printMyName();
        int amount = clientService.deleteClientIfOutdatedRequest();
        assertEquals(1, amount);
    }
}
