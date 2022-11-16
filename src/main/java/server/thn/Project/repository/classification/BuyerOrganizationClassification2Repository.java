package server.thn.Project.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.buyerOrgClassification.BuyerOrganizationClassification1;
import server.thn.Project.entity.buyerOrgClassification.BuyerOrganizationClassification2;

import java.util.List;

public interface BuyerOrganizationClassification2Repository extends JpaRepository<BuyerOrganizationClassification2,Long> {
    List<BuyerOrganizationClassification2>
    findByTeamClassification1
            (BuyerOrganizationClassification1 buyerOrganizationClassification1);

}
