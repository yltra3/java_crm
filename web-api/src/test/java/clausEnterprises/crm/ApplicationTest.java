package clausEnterprises.crm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static clausEnterprises.crm.TestUtils.printMyName;

@RunWith(SpringRunner.class)
public class ApplicationTest extends AbstractIntegrationTest {
    @Test
    public void contextLoads() {
        printMyName();
    }
}
