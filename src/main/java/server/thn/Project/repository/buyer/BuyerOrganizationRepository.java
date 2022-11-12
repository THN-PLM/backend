package server.thn.Project.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.BuyerOrganization;

public interface BuyerOrganizationRepository extends JpaRepository<BuyerOrganization, Long>, CustomBuyerOrganizationRepository{

}
