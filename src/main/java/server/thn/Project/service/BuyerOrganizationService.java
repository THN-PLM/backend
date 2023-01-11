package server.thn.Project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Project.dto.C1SelectDto;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.dto.produceOrg.ProduceOrganizationListDto;
import server.thn.Project.entity.buyer.BuyerOrganization;
import server.thn.Project.exception.ProduceOrganizationNotFoundException;
import server.thn.Project.repository.buyer.BuyerOrganizationRepository;
import server.thn.Project.repository.classification.BuyerOrganizationClassification1Repository;
import server.thn.Project.repository.classification.BuyerOrganizationClassification2Repository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerOrganizationService {

    private final BuyerOrganizationRepository buyerOrganizationRepository;
    private final BuyerOrganizationClassification1Repository buyerOrganizationClassification1Repository;
    private final BuyerOrganizationClassification2Repository buyerOrganizationClassification2Repository;

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

    public List<C1SelectDto> readAllBuyerOrganizationClassification1(

    ) {
        return  C1SelectDto.toBuyerOrgDtoList(
                buyerOrganizationClassification1Repository.findAll(),
                buyerOrganizationClassification2Repository
        );
    }

}

