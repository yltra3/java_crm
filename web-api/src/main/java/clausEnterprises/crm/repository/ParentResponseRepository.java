package clausEnterprises.crm.repository;

import clausEnterprises.crm.consts.enums.Behaviour;
import clausEnterprises.crm.model.ParentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentResponseRepository extends JpaRepository<ParentResponse, Long> {

    @Query(value = "select pr " +
            "from ParentResponse pr " +
            "where pr.children.email = :email " +
            "           and pr.children.parentEmail = :parentEmail ")
    ParentResponse getParentResponseBySingleClient(@Param("email") String email,
                                                   @Param("parentEmail") String parentEmail);

    @Query(value = "select pr " +
            "from ParentResponse pr " +
            "where pr.behaviour = :behaviour ")
    List<ParentResponse> getUnclearParentResponses(@Param("behaviour") Behaviour behaviour);

    @Query(value = "select pr " +
            "from ParentResponse pr " +
            "where pr.behaviour <> :behaviour ")
    List<ParentResponse> getClearResponses(@Param("behaviour") Behaviour behaviour);
}