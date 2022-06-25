package clausEnterprises.crm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@SuperBuilder
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"sendTo", "subject"})
public class MailDto {
    private String sendTo;
    private String subject;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Min(value = 5, message = "Value should be greater than 5")
    @Max(value = 18, message = "Value should me less than 18")
    private BigDecimal age;
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Wrong email format. Please try again")
    private String email;
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Wrong email format. Please try again")
    private String parentEmail;
    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String address;
    @NotNull
    private String message;
}

