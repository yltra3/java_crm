package clausEnterprises.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreationDto {
    @NotNull
    @Min(value = 0L, message = "Value can't be less than 0")
    @Max(value = Long.MAX_VALUE, message = "Value is too big")
    private Long id;
    private List<?> dtos;
}
