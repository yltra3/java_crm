package clausEnterprises.crm.repository;

import clausEnterprises.crm.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Courier, Long> {
    @Query("SELECT cs " +
            "FROM Courier cs " +
            "WHERE cs.id= :id")
    Courier getCourierStatsForCertainCourier(@Param("id") Long courierId);

}
