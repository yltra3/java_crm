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
@Table(schema = "CLAUSDB", name = "PARENT_RESPONSE")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParentResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "children_id")
    @OneToOne(cascade = CascadeType.ALL)
    private Client children;
    @Column(name = "behaviour")
    @Enumerated(EnumType.STRING)
    private Behaviour behaviour;
    @Column(name = "date_Created")
    private LocalDateTime dateCreated;
}
