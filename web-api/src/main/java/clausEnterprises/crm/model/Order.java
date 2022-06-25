package clausEnterprises.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(schema = "CLAUSDB", name = "DELIVERY_ORDER")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "children_id")
    @OneToOne(cascade = CascadeType.ALL)
    private Client children;
    @Column(name = "country")
    private String country;
    @Column(name = "address")
    private String address;
    @JoinColumn(name = "courier_Id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Courier courier;
    @JoinColumn(name = "gift_Id")
    @OneToOne(cascade = CascadeType.ALL)
    private Warehouse gift;
    @Column(name = "date_Created")
    private LocalDateTime dateCreated;
    @Column(name = "is_Actual")
    private Boolean isActual;

}
