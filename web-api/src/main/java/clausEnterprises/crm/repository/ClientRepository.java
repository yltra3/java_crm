package clausEnterprises.crm.repository;

import clausEnterprises.crm.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("select c " +
            "from Client c " +
            "where c.email = :email and c.lastTimeDelivered is not null")
    Client getAlreadyExistingClient(@Param("email") String email);

    @Query("delete from Client c " +
            "where c.email = :email " +
            "and c.parentEmail = :parentEmail ")
    @Modifying
    void deleteClientByBothEmails(@Param("email") String email,
                                  @Param("parentEmail") String parentEmail);
}
