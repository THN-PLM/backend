package server.thn.Project.repository.carType;

import org.springframework.data.domain.Page;
import server.thn.Project.dto.carType.CarTypeReadCondition;
import server.thn.Project.dto.carType.CarTypeReadResponse;

public interface CustomCarTypeRepository {
    Page<CarTypeReadResponse> findAllByCondition(CarTypeReadCondition cond);
}


