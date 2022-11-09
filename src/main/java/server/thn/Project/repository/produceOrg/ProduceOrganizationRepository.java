package server.thn.Project.repository.produceOrg;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification;

public interface ProduceOrganizationRepository extends JpaRepository<ProduceOrganizationClassification, Long> {

}
