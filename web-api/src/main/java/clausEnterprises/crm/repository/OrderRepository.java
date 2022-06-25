package clausEnterprises.crm.repository;

import clausEnterprises.crm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.isActual = true ")
    List<Order> getAvailableOrders();

    @Query("select o from Order o where o.id = :id")
    Order getOrderById(@Param("id") Long id);

    @Query("select o.id from Order o ")
    List<Long> getAllOrdersId();

}
