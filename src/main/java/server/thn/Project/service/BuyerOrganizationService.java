package server.thn.Project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.dto.produceOrg.ProduceOrganizationListDto;
import server.thn.Project.entity.BuyerOrganization;
import server.thn.Project.entity.ProduceOrganization;
import server.thn.Project.exception.ProduceOrganizationNotFoundException;
import server.thn.Project.repository.buyer.BuyerOrganizationRepository;
import server.thn.Project.repository.produceOrg.ProduceOrganizationRepository;

@Service
@RequiredArgsConstructor
public class BuyerOrganizationService {

    private final BuyerOrganizationRepository buyerOrganizationRepository;

    public ProduceOrganizationListDto readAll(ReadCondition cond) {
        return ProduceOrganizationListDto.toBuyterDto(
                buyerOrganizationRepository.findAllByCondition(cond)
        );
    }

    @Transactional
    public void delete(Long id) {
        BuyerOrganization buyerOrganization = buyerOrganizationRepository.findById(id).orElseThrow(ProduceOrganizationNotFoundException::new);
        buyerOrganization.setNotDeleted(false);
    }


}

