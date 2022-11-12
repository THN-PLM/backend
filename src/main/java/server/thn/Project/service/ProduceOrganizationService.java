package server.thn.Project.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Item.dto.ItemCreateResponse;
import server.thn.Project.dto.IdNameDto;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.dto.produceOrg.ProduceOrganizationListDto;
import server.thn.Project.entity.ProduceOrganization;
import server.thn.Project.exception.ProduceOrganizationNotFoundException;
import server.thn.Project.repository.produceOrg.ProduceOrganizationRepository;

@Service
@RequiredArgsConstructor
public class ProduceOrganizationService {

    private final ProduceOrganizationRepository produceOrganizationRepository;

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


}
