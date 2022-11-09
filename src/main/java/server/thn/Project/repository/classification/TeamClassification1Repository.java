package server.thn.Project.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification1;

import java.util.List;

public interface TeamClassification1Repository extends JpaRepository<ProduceOrganizationClassification1, Long> {
    @Query("select c from TeamClassification1 c join fetch c.teamClassification2List ")
    List<ProduceOrganizationClassification1> findAllByTeamClassification1();

}