package server.thn.Project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Project.dto.C1SelectDto;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.dto.produceOrg.ProduceOrganizationListDto;
import server.thn.Project.entity.producer.ProduceOrganization;
import server.thn.Project.exception.ProduceOrganizationNotFoundException;
import server.thn.Project.repository.classification.ProduceOrganizationClassification1Repository;
import server.thn.Project.repository.classification.ProduceOrganizationClassification2Repository;
import server.thn.Project.repository.produceOrg.ProduceOrganizationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduceOrganizationService {

    private final ProduceOrganizationRepository produceOrganizationRepository;
    private final ProduceOrganizationClassification1Repository produceOrganizationClassification1Repository;
    private final ProduceOrganizationClassification2Repository produceOrganizationClassification2Repository;

    public ProduceOrganizationListDto readAll(ReadCondition cond) {
        return ProduceOrganizationListDto.toDto(
                produceOrganizationRepository.findAllByCondition(cond)
        );
    }

//
//    public ItemCreateResponse create(IdNameDto cond) {
//
//        ProduceOrganization produceOrganization = produceOrganizationRepository.save(
//                IdNameDto.toProduceOrganizationEntity(
//                        cond
//                )
//        );
//
//        return new ItemCreateResponse(
//                produceOrganization.getId()
//        );
// }

    @Transactional
    public void delete(Long id) {
        ProduceOrganization produceOrganization = produceOrganizationRepository.findById(id).orElseThrow(ProduceOrganizationNotFoundException::new);
        produceOrganization.setNotDeleted(false);
    }


    /**
     * PROJECT ORGANIZATION 트리 구조 데려오기 !
     * @return
     */
    public List<C1SelectDto> readAllProduceOrganizationClassification1(

    ) {
        return  C1SelectDto.toProduceOrgDtoList(
                produceOrganizationClassification1Repository.findAll(),
                produceOrganizationClassification2Repository
        );
    }


}
