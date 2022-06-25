package clausEnterprises.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(schema = "CLAUSDB", name = "COURIER")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "active_Deliveries")
    private Long activeDeliveries;
    @JoinColumn(name = "courier_id")
    @OneToOne(cascade = CascadeType.ALL)
    private User courier;
    @Column(name = "times_Delivered")
    private Long timesDelivered;
    @Column(name = "times_Failed")
    private Long timesFailed;
    @Column(name = "efficiency")
    private BigDecimal efficiency;
}
