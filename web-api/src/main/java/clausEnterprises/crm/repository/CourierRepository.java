package clausEnterprises.crm.repository;

import clausEnterprises.crm.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    @Query("select cs " +
            "from Courier cs " +
            "where cs.id = :id")
    Courier getCourierById(@Param("id") Long id);

    @Query("select cs.id from Courier cs ")
    List<Long> getAllCouriersId();

}
