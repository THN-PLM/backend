package server.thn.Project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.dto.carType.CarTypeListDto;
import server.thn.Project.dto.carType.CarTypeReadCondition;
import server.thn.Project.dto.produceOrg.ProduceOrganizationListDto;
import server.thn.Project.entity.CarType;
import server.thn.Project.exception.CarTypeNotFoundException;
import server.thn.Project.repository.carType.CarTypeRepository;

@Service
@RequiredArgsConstructor
public class CarTypeService {

    private final CarTypeRepository carTypeRepository;

    public CarTypeListDto readAll(CarTypeReadCondition cond) {
        return CarTypeListDto.toDto(
                carTypeRepository.findAllByCondition(cond)
        );
    }


    @Transactional
    public void delete(Long id) {
        CarType carType = carTypeRepository.findById(id).orElseThrow(CarTypeNotFoundException::new);
        carType.setNotDeleted(false);
    }

//    public ItemCreateResponse create(IdNameDto cond) {
//
//        CarType CarType = carTypeRepository.save(
//                IdNameDto.toCarTypeEntity(
//                        cond
//                )
//        );
//
//        return new ItemCreateResponse(
//                CarType.getId()
//        );
//    }
}
