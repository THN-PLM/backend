package server.thn.Project.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.produceOrgClassification.*;

public interface TeamClassificationRepository extends JpaRepository<ProduceOrganizationClassification, Long> {

    ProduceOrganizationClassification
    findByClassification1AndClassification2(
            ProduceOrganizationClassification1 classification1,
            ProduceOrganizationClassification2 classification2zz
    );

}
