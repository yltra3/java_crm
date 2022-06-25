package clausEnterprises.crm.repository;

import clausEnterprises.crm.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("select wh from Warehouse wh where wh.id = :id")
    Warehouse getGiftById(@Param("id") Long id);

    @Query("select wh.id from Warehouse wh where wh.is_available = true")
    List<Long> getAllGiftsId();
}
