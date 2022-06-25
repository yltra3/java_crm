package clausEnterprises.crm.service;

import clausEnterprises.crm.AbstractIntegrationTest;
import clausEnterprises.crm.consts.enums.Behaviour;
import clausEnterprises.crm.model.ParentResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static clausEnterprises.crm.TestUtils.printMyName;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Sql({"/parentResponseTest.sql"})
public class ParentResponseServiceTest extends AbstractIntegrationTest {
    @Autowired
    private ParentResponseService parentResponseService;

    @Test
    public void getExistingResponseByEmailsTest() {
        printMyName();
        ParentResponse result = parentResponseService.getResponseByEmails("dantab2@gmail.com",
                "dantabz@gmail.com");
        assertNotNull(result);
        assertEquals("name2", result.getChildren().getFirstName());
        assertEquals("lastname2", result.getChildren().getLastName());
        assertEquals("dantab2@gmail.com", result.getChildren().getEmail());
        assertEquals("dantabz@gmail.com", result.getChildren().getParentEmail());
        assertEquals(Behaviour.GOOD, result.getBehaviour());
    }

    @Test
    public void getNonExistingResponseByEmailsTest() {
        printMyName();
        ParentResponse resultFirst = parentResponseService.getResponseByEmails("dantab2@gmail.com",
                "dantab2@gmail.com");
        ParentResponse resultSecond = parentResponseService.getResponseByEmails("dantabz@gmail.com",
                "dantabz@gmail.com");
        ParentResponse resultThird = parentResponseService.getResponseByEmails("dantabz@gmail.com",
                "dantab2@gmail.com");
        assertNull(resultFirst);
        assertNull(resultSecond);
        assertNull(resultThird);
    }

    @Test
    public void getAllCheckedResponsesTest() {
        printMyName();
        long count = parentResponseService.getAllCheckedResponses().size();
        assertEquals(3, count);
    }
}
