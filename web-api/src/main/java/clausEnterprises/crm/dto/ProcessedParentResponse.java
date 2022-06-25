package clausEnterprises.crm.dto;

import clausEnterprises.crm.consts.enums.Behaviour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@SuperBuilder
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedParentResponse {
    private String clientEmail;
    private String parentEmail;
    @Enumerated(EnumType.STRING)
    private Behaviour behaviour;
}
