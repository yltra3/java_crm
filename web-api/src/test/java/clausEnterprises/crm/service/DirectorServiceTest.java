package clausEnterprises.crm.service;

import clausEnterprises.crm.AbstractIntegrationTest;
import clausEnterprises.crm.dto.CreationDto;
import clausEnterprises.crm.dto.ReportResultDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static clausEnterprises.crm.TestUtils.printMyName;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@Sql({"/directorTest.sql"})
public class DirectorServiceTest extends AbstractIntegrationTest {
    @Autowired
    private DirectorService directorService;

    @Test
    public void testGetReportForAllCouriers() {
        printMyName();
        List<ReportResultDto> list = directorService.getReportForAllCouriers();
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testGetReportForASingleCourier() {
        printMyName();
        ReportResultDto dto1 = directorService.getReportForASingleUser(CreationDto.builder()
                .id(1L)
                .build());
        ReportResultDto dto2 = directorService.getReportForASingleUser(CreationDto.builder()
                .id(2L)
                .build());
        Assert.assertNotNull(dto1);
        Assert.assertNotNull(dto2);
        Assert.assertEquals("Vitalya", dto1.getFirstname());
        Assert.assertEquals("Stas", dto2.getFirstname());
        Assert.assertEquals(BigDecimal.valueOf(40.00), dto1.getEfficiency());
        Assert.assertEquals(BigDecimal.valueOf(20.00), dto2.getEfficiency());
    }
}
