package server.thn.Route.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.thn.Route.entity.RouteOrdering;
import server.thn.Route.entity.RouteProduct;

import java.util.List;

public interface RouteProductRepository extends JpaRepository<RouteProduct, Long> {

    @Query("select c from RouteProduct c " +
            "where c.routeOrdering = :routeOrdering " +
            "order by c.id asc nulls first, c.id asc")
    List<RouteProduct> findAllByRouteOrdering(@Param("routeOrdering") RouteOrdering routeOrdering);


    @Query(
            "select i from RouteProduct " +
                    "i where i IN (:routeProducts) " +
                    "order by i.id asc nulls first, i.id asc"
    )
    Page<RouteProduct> findByRouteProducts(@Param("routeProducts") List<RouteProduct> routeProducts, Pageable pageable);


    @Query(
            "select i from RouteProduct " +
                    "i where i IN (:routeProducts) " +
                    "order by i.id asc nulls first, i.id asc"
    )
    List<RouteProduct> findByRouteProduct(@Param("routeProducts") List<RouteProduct> routeProducts);


    @Query(
            "select i from RouteProduct " +
                    "i where i IN (:routeProducts) " +
                    "order by i.id asc nulls first, i.id asc"
    )
    List<RouteProduct> findByRouteProducts(@Param("routeProducts") List<RouteProduct> routeProducts);


}
