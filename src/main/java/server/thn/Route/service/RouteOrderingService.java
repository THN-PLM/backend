package server.thn.Route.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Item.dto.ItemCreateResponse;
import server.thn.Member.dto.MemberDto;
import server.thn.Member.entity.Member;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Member.repository.MemberRepository;
import server.thn.Project.entity.ProjectTypeEnum;
import server.thn.Project.exception.ProjectTypeNotFoundException;
import server.thn.Project.repository.ProjectRepository;
import server.thn.Project.repository.ProjectTypeRepository;
import server.thn.Project.service.ProjectService;
import server.thn.Route.dto.routeOrdering.*;
import server.thn.Route.dto.routeProduct.RouteProductCreateRequest;
import server.thn.Route.dto.routeProduct.RouteProductDto;
import server.thn.Route.dto.routeProduct.RouteProductResponsiblePassDto;
import server.thn.Route.entity.RouteOrdering;
import server.thn.Route.entity.RoutePreset;
import server.thn.Route.entity.RouteProduct;
import server.thn.Route.exception.RouteNotFoundException;
import server.thn.Route.exception.RouteProductNotFoundException;
import server.thn.Route.exception.UpdateImpossibleException;
import server.thn.Route.repository.RouteOrderingRepository;
import server.thn.Route.repository.RouteProductMemberRepository;
import server.thn.Route.repository.RouteProductRepository;
import server.thn.Route.repository.RouteTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteOrderingService {

    @Value("${default.image.address}")
    private String defaultImageAddress;

    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final RoutePreset routePreset;
    private final ProjectTypeRepository projectTypeRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final RouteProductMemberRepository routeProductMemberRepository;
    private final RouteTypeRepository routeTypeRepository;
    private final ProjectService projectService;

    public RouteOrderingDto read(Long id) {

        RouteRejectPossibleResponse routeRejectPossibleResponse =
                rejectPossible(id);

        return RouteOrderingDto.toDto(
                routeOrderingRepository.findById(id).orElseThrow(RouteNotFoundException::new),
                routeProductRepository,
                routeOrderingRepository,
                routeRejectPossibleResponse,
                defaultImageAddress
        );

    }

    public List<List<MemberDto>> memberRead(Long routeId) {

        Set<String> names =
                read(routeId).getRouteProductList().stream().map(
                        i -> i.getName()
                ).collect(Collectors.toSet());

        List<String> nameList = new ArrayList<>(names); // 라우트 프로덕트 리스트의 길이
        List<List<MemberDto>> mem = new ArrayList<>();

        int idx = 0;
        while (idx < nameList.size() - 1) {
            for (RouteProductDto rp : read(routeId).getRouteProductList().subList(0,nameList.size())) {
                if (nameList.get(idx).equals(rp.getName())) {
                    mem.add(
                            rp.getMember()
                    );
                }
                idx += 1;
            }
        }

        return mem;
    }

    @Transactional
    public RouteRejectPossibleResponse rejectPossible(Long id) {

        RouteOrdering routeOrdering = routeOrderingRepository
                .findById(id)
                .orElseThrow(RouteNotFoundException::new);


        //라우트 오더링의 라우트 리스트
        List<RouteProduct> presentRouteProductCandidate = routeProductRepository
                .findAllByRouteOrdering(routeOrdering);

        if (routeOrdering.getPresent() == presentRouteProductCandidate.size()) {
            List<SeqAndName> tmpList = new ArrayList<>();
            return new RouteRejectPossibleResponse(tmpList);
        }

        //라우트 리스트 처음부터 거절 주체 전까지
        List<RouteProduct> routeProductRejectCandidates =
                presentRouteProductCandidate.subList(0, routeOrdering.getPresent());

        //CASE1 ) 거절 주체가 되는 라우트
        RouteProduct targetRoutProduct = presentRouteProductCandidate.get(routeOrdering.getPresent());

        List<SeqAndName> seqAndNameList = new ArrayList<>();

        // 11/12 여기 아래 무시당할 예정
        if (routePreset.reviewRouteArrList.contains(targetRoutProduct.getType().getId().toString())) {
            //만약 리뷰타입의 라우트라면
            Long rejectPossibleTypeId = null;

            //아래 부분 db 에 라우트 타입따라서 바뀌는 것이었음
            switch (targetRoutProduct.getType().getId().toString()) {

                case "4":         // 아이템 리뷰인 경우
                    rejectPossibleTypeId = 1L;
                    break;
                case "6":            // 플젝 리뷰
                    rejectPossibleTypeId = 9L;
                    break;
                case "5":            // 디자인 리뷰
                    rejectPossibleTypeId = 11L;
                    break;
                case "10":            // 봄 리뷰
                    rejectPossibleTypeId = 9L;
                    break;

                case "14":            // CR신청 APPROVE
                case "15":            // CR REVIEW
                    rejectPossibleTypeId = 13L;
                    break;

                case "17":
                    rejectPossibleTypeId = 16L; //CO 신청
                    break;

                case "21":
                    rejectPossibleTypeId = 20L; //release request
                    break;

                case "23":
                    rejectPossibleTypeId = 22L; //document request

                    break;
                default:        // 모두 해당이 안되는 경우
                    rejectPossibleTypeId = 0L;
                    break;
            }

            for (RouteProduct routeProduct : routeProductRejectCandidates) {
                System.out.println(routeProduct.getType().getId());
                if (Objects.equals(routeProduct.getType().getId(),
                        rejectPossibleTypeId)
                        && !(routeProduct.isDisabled())) {
                    seqAndNameList.add(
                            new SeqAndName(
                                    routeProduct.getSequence(),
                                    routeProduct.getRoute_name()
                            ));
                }

            }

        }
        return new RouteRejectPossibleResponse(seqAndNameList);
    }

    // 프젝 타입에 따른 라우트오더링 리스트 다르다

    public List readRouteByProj(Long id) {

        List<String> typeList = new ArrayList<>();

        Integer routeType = ProjectTypeEnum.valueOf(
                projectTypeRepository.findById(id).orElseThrow(ProjectTypeNotFoundException::new)
                        .getProjectType().name()
        ).label();

        List routeProduct = List.of((routePreset.projectRouteName[routeType]));

        for (Object type : routeProduct) {
            typeList.add(type.toString());
        }
        return typeList;

    }

    @Transactional
    public ItemCreateResponse routeProductAddResponsibilityMember(RouteProductResponsiblePassDto req) {

        for(Long memberId : req.getMemberId()) {
            Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

            for (Long routeProductId : req.getRouteIds()) {

                RouteProduct routeProduct = routeProductRepository
                        .findById(routeProductId).orElseThrow(RouteProductNotFoundException::new);

                // 권한이 없을 때만 추가되도록 하긔
                if(routeProductMemberRepository.findByRouteProductAndMember(routeProduct, member).size()==0) {
                    routeProduct.addMember(member);
                }
            }
        }
        return new ItemCreateResponse(200L);
    }

    public List<RouteOrderingDto> readAll(RouteOrderingReadCondition cond) {

        List<RouteOrdering> newRoutes = routeOrderingRepository.findByProjectOrderByIdAsc(
                projectRepository.findById(cond.getItemId())
                        .orElseThrow(RouteNotFoundException::new)
        );

        return RouteOrderingDto.toDtoList(
                newRoutes,
                routeProductRepository,
                routeOrderingRepository,
                defaultImageAddress
        );

    }

    @Transactional
    public RouteOrderingCreateResponse createProjectRoute(RouteOrderingCreateRequest req) {

        RouteOrdering newRoute = routeOrderingRepository.save(RouteOrderingCreateRequest.toProjectEntity(
                        req,
                        routePreset,
                        projectRepository
                )
        );

        List<RouteProduct> routeProductList =
                RouteProductCreateRequest.toProjectEntityList(
                        req,
                        newRoute,
                        routePreset,
                        memberRepository,
                        routeTypeRepository

                );

        for (RouteProduct routeProduct : routeProductList) {

            RouteProduct routeProduct1 =
                    routeProductRepository.save(routeProduct);

            System.out.println(routeProduct1.getRoute_name());
            System.out.println(routeProduct1.getMembers().get(0).getMember());
            System.out.println(routeProduct1.getMembers().get(0).getRouteProduct());
        }

        newRoute.getProject().updateTempSaveReadOnlyWhenMadeRoute();//라우트 만들면 임시저장 해제

        newRoute.updateToComplete();

        return new RouteOrderingCreateResponse(newRoute.getId());
    }

    @Transactional
    public RouteUpdateResponse approveUpdate(Long id, RouteOrderingUpdateRequest req) {

        RouteOrdering routeOrdering = routeOrderingRepository
                .findById(id)
                .orElseThrow(RouteNotFoundException::new);


        List<RouteProduct> presentRouteProductCandidate = routeProductRepository
                .findAllByRouteOrdering(routeOrdering);

        //현재 진행중인 라우트프로덕트

        if (routeOrdering.getLifecycleStatus().equals("COMPLETE")) {
            throw new UpdateImpossibleException();
        }

        if (presentRouteProductCandidate.size() == routeOrdering.getPresent()) {
            //만약 present 가 끝까지 닿았으면 현재 complete 된 상황!
            routeOrdering.updateToComplete();

        } else {
            RouteProduct targetRoutProduct = presentRouteProductCandidate.get(routeOrdering.getPresent());

            // 06-17 리팩토링 : module : DESIGN , name : create
            if (targetRoutProduct.getType().getModule().equals("PROJECT")
                    && targetRoutProduct.getType().getName().equals("TIME")) {

                    //만약 지금 rejected 가 true였다면 , 이제 새로 다시 넣어주는 것이니깐 rejected풀어주기
                    if (targetRoutProduct.isPreRejected()) {
                        targetRoutProduct.setPreRejected(false); //06-01 손댐
                    }

            }
            }
            RouteOrderingUpdateRequest newRouteUpdateRequest =
                    routeOrdering
                            .update(
                                    req,
                                    routeProductRepository,
                                    routeOrderingRepository,
                                    projectRepository,
                                    projectService
                            );

        return new RouteUpdateResponse(id);
        }

        //

    @Transactional
    public List<RouteProductDto> rejectUpdate(
            Long id,
            String rejectComment,
            Integer rejectedSequence
    ) {

        RouteOrdering routeOrdering = routeOrderingRepository.findById(id).orElseThrow(RouteNotFoundException::new);

        List<RouteProduct> rejectUpdatedRouteProductList = routeOrdering.rejectUpdate(
                id,
                rejectComment,
                rejectedSequence,
                routeOrderingRepository,
                routeProductRepository

        );

        List<RouteProduct> addedProducts = new ArrayList<>();

        // 새로 만들어진 애들 저장
        for (RouteProduct routeProduct : rejectUpdatedRouteProductList) {
            addedProducts.add(routeProductRepository.save(routeProduct));
        }

        addedProducts.get(0).setRejected(false);

        List<RouteProduct> deletedList =
                //isShow 가 false 인 것은 삭제 처리
                routeProductRepository.findAllByRouteOrdering(routeOrdering)
                        .subList(
                                routeOrdering.getPresent() - 1,
                                routeProductRepository.findAllByRouteOrdering(routeOrdering).size()
                        )
                        .stream().filter(
                                d -> !d.isRoute_show()
                        )
                        .collect(
                                Collectors.toList()
                        );

        //본격 삭제 진행
        for (RouteProduct routeProduct : deletedList) {
            routeProductRepository.delete(routeProduct);
        }

        // 기존 + 새 라우트프로덕트까지 해서 돌려주기
        return RouteProductDto.toProductDtoList(
                routeProductRepository.findAllByRouteOrdering(routeOrdering),
                defaultImageAddress
        );
    }

}
