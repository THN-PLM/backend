package server.thn.Project.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.buyer.BuyerOrganization;

public interface BuyerOrganizationRepository extends JpaRepository<BuyerOrganization, Long>, CustomBuyerOrganizationRepository{

}
