package server.thn.Project.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.producer.produceOrgClassification.ProduceOrganizationClassification1;
import server.thn.Project.entity.producer.produceOrgClassification.ProduceOrganizationClassification2;

import java.util.List;

public interface ProduceOrganizationClassification2Repository extends
        JpaRepository<ProduceOrganizationClassification2, Long> {

    List<ProduceOrganizationClassification2>
        findByProduceOrganizationClassification1
            (ProduceOrganizationClassification1 produceOrganizationClassification1);

}
