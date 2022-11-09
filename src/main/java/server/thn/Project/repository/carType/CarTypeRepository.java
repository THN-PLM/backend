package server.thn.Project.repository.carType;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.CarType;

public interface CarTypeRepository extends JpaRepository<CarType, Long>, CustomCarTypeRepository {
}

