package server.thn.Route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Route.entity.RouteType;

import java.util.List;

public interface RouteTypeRepository extends JpaRepository<RouteType, Long> {

    List<RouteType> findByName(String name);

    RouteType findByModuleAndName(String moduleName, String name);

}
