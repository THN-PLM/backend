package server.thn.Route.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Member.entity.Member;
import server.thn.Route.entity.RouteProduct;
import server.thn.Route.entity.RouteProductMember;

import java.util.List;

public interface RouteProductMemberRepository extends JpaRepository<RouteProductMember , Long> {
    List<RouteProductMember> findByMember(Member member);

    Page<RouteProductMember> findByMember(Member member, Pageable pageable);

    List<RouteProductMember> findByRouteProductAndMember(RouteProduct routeProduct, Member member);

}
