package clausEnterprises.crm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReportResultDto {
    private String firstname;
    private String lastname;
    private Long deliveriesDone;
    private Long deliveriesFailed;
    private BigDecimal efficiency;
}
