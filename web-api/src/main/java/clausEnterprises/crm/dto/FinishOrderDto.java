package clausEnterprises.crm.dto;

import clausEnterprises.crm.consts.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishOrderDto {
    @NotNull
    private Long orderId;
    @NotNull
    private Long courierId;
    @NotNull
    private OrderStatus orderStatus;
}
