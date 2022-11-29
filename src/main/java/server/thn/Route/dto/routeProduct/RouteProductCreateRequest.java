package server.thn.Route.dto.routeProduct;


import org.springframework.util.RouteMatcher;
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
import server.thn.Route.entity.RouteType;
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
        List<String> routeProductName = List.of((routePreset.projectRouteName[projectTypeId-1]));
        List<String> routeProductType = List.of((routePreset.projectRouteType[projectTypeId-1]));
        List<String> routeProductTypeModule = List.of((routePreset.projectRouteTypeModule[projectTypeId-1]));

        // 멤버 적절히 할당해줬는지 체크

        Integer neededRouteProductCnt = routeProductType.size()-1;

        if (req.getMemberIds()!=null && neededRouteProductCnt - 1 < req.getMemberIds().size()) {
            //멤버가 할당되지 않아서 짝이 안맞아
            throw new MemberOverAssignedException();
        }

        if (neededRouteProductCnt - 1 > req.getMemberIds().size()) {
            //멤버가 할당되지 않아서 짝이 안맞아
            throw new MemberNotAssignedException();
        }

        List<Member> emptyRouteMember = new ArrayList<>();
        //DB에 꼭 멤버 아이디 -1 인 애 생성해주기
        emptyRouteMember.add(memberRepository.findById(-1L).orElseThrow(MemberNotFoundException::new));

        // 1) request RouteProduct는 별도 생성 (코멘트 및 멤버 지정 이슈)
        List<Member> member1 = new ArrayList<>();

        member1.add(memberRepository.findById(req.getMemberId())
                .orElseThrow(MemberNotFoundException::new));

        RouteProduct requestRouteProduct = new RouteProduct(
                0,
                0,

                (String) routeProductName.get(0),

                //우선 NAME 으로 찾아와서
                routeTypeRepository.findByName(routeProductName.get(0))
                        .get(0),
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

        // 2) 나머지 rest route 생성
        List<RouteProduct> restRouteProducts = new ArrayList<>();
        int index = 0;

        for(List list : req.getMemberIds()){

            Integer finalIndex = index;
            RouteProduct routeProduct = new RouteProduct(
                    index+1,
                    index+1,
                    (String) routeProductName.get(index+1),

                    routeTypeRepository.findByName((String) routeProductName.get(index+1)).
                            stream().filter(
                                    m-> m.getModule().equals(
                                            routeProductTypeModule.get(finalIndex+1)
                                    )
                            ).collect(toList()).get(0),


                    " ",
                    false,
                    false,//reject
                    false,
                    true,
                    false,
                    -1,
                    req.getMemberIds().get(index)==null?emptyRouteMember
                            :req.getMemberIds().get(index)//memberIds에서는 0부터 시작(request member 포함x)
                            .stream().map(
                                    m->
                                            memberRepository.findById(m).
                                                    orElseThrow(MemberNotFoundException::new)
                            ).collect(
                                    toList()
                            ),
                    routeOrdering

            );

            restRouteProducts.add(routeProduct);

            index+=1;
        }
        //

        willBeSavedRouteProductList.add(requestRouteProduct);

        willBeSavedRouteProductList.addAll(restRouteProducts);

        return willBeSavedRouteProductList;

    }
}
