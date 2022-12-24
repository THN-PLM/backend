package server.thn.Project.repository.produceOrg;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.ProduceOrganization;

public interface ProduceOrganizationRepository extends JpaRepository<ProduceOrganization, Long> ,CustomProduceOrganizationRepository {
}
