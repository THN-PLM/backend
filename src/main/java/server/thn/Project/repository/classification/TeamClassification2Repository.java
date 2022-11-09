package server.thn.Project.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification1;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification2;

import java.util.List;

public interface TeamClassification2Repository extends JpaRepository<ProduceOrganizationClassification2, Long> {
    List<ProduceOrganizationClassification2> findByTeamClassification1(ProduceOrganizationClassification1 classification1);

}
