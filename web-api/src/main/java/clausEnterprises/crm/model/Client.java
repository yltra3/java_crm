package clausEnterprises.crm.model;

import clausEnterprises.crm.consts.enums.Behaviour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(schema = "CLAUSDB", name = "CLIENT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_Name")
    private String firstName;
    @Column(name = "last_Name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "parent_Email")
    private String parentEmail;
    @Column(name = "address")
    private String address;
    @Column(name = "behaviour")
    @Enumerated(EnumType.STRING)
    private Behaviour behaviour;
    @Column(name = "country")
    private String country;
    @Column(name = "last_Time_Delivered")
    private LocalDateTime lastTimeDelivered;
}
