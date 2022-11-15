package server.thn.Route.dto.routeProduct;


import server.thn.Member.entity.Member;
import server.thn.Member.exception.MemberNotAssignedException;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Member.exception.MemberOverAssignedException;
import server.thn.Member.repository.MemberRepository;
import server.thn.Project.entity.ProjectTypeEnum;
import server.thn.Route.dto.routeOrdering.RouteOrderingCreateRequest;
import server.thn.Route.entity.RouteOrdering;
import server.thn.Route.entity.RoutePreset;
import server.thn.Route.entity.RouteProduct;
import server.thn.Route.repository.RouteTypeRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class RouteProductCreateRequest{

    public static List<RouteProduct> toProjectEntityList(
            RouteOrderingCreateRequest req,
            RouteOrdering routeOrdering,
            RoutePreset routePreset,
            MemberRepository memberRepository,
            RouteTypeRepository routeTypeRepository

    ) {
        List<RouteProduct> willBeSavedRouteProductList
            = new ArrayList<>();

        Integer projectTypeId = Math.toIntExact(routeOrdering.getProject().getProjectType().getId());
        List<String> routeProductName = List.of((routePreset.projectRouteName[projectTypeId]));
        List<String> routeProductType = List.of((routePreset.projectRouteType[projectTypeId]));
        List<String> routeProductTypeModule = List.of((routePreset.projectRouteTypeModule[projectTypeId]));

        Integer neededRouteProductCnt = routeProductType.size()-1; //프로젝트는 오로지 한개

        // 1) request RouteProduct는 별도 생성 (코멘트 및 멤버 지정 이슈)

        List<Member> member1 = new ArrayList<>();

        member1.add(memberRepository.findById(req.getMemberId())
                .orElseThrow(MemberNotFoundException::new));

        RouteProduct requestRouteProduct = new RouteProduct(
                0,
                0,

                (String) routeProductName.get(0),

                //우선 NAME 으로 찾아와서
                routeTypeRepository.findByName(routeProductType.get(0)).
                        stream().filter(
                                i-> i.getModule().equals(
                                        //그것이 지정된 모듈이랑 SAME 한 라우트 타입 최종 고르기
                                        routeProductTypeModule.get(0)
                                )
                        ).collect(toList()).get(0),
                req.getRequestComment(),
                false,
                false,//reject
                false,
                true,
                false,
                -1,
                member1,
                routeOrdering

        );

        int seq = 0;
        willBeSavedRouteProductList.add(requestRouteProduct);

        return willBeSavedRouteProductList;

    }
}
