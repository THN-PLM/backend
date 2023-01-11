package server.thn.Route.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;
import server.thn.Common.entity.EntityDate;
import server.thn.Project.dto.ProjectCreateRequest;
import server.thn.Project.entity.Project;
import server.thn.Project.repository.ProjectRepository;
import server.thn.Project.service.ProjectService;
import server.thn.Route.dto.routeOrdering.RouteOrderingUpdateRequest;
import server.thn.Route.exception.RejectImpossibleException;
import server.thn.Route.exception.UpdateImpossibleException;
import server.thn.Route.repository.RouteOrderingRepository;
import server.thn.Route.repository.RouteProductRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Component
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class RouteOrdering extends EntityDate {

    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)

    private Long id;

    /**
     * 요청 int로 받아서 지정할 때
     * routePreset.get(int)로 지정
     */
    @Column(nullable = false)
    private String type;

    /**
     * route에 딸린 routeProduct리스트의
     * 맨 마지막 인덱스 값 아이의 passed = 1 이면 complete,
     * passed = 0 이면 development
     * 나중에 갱신진행 시때도 development로
     */
    @Column(nullable = false)
    private String lifecycleStatus;

    /**
     * 처음엔 0, 갱신 요청 들어올 때마다 +1
     */
    @Column(nullable = false)
    private Integer revisedCnt;

    /**
     * 현재 진행중인 routeproduct의 인덱스
     */
    @Column(nullable = false)
    private Integer present;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    //프로젝트 라우트용 생성자
    public RouteOrdering(
            String type,
            Project project

    ){
        this.type = type;
        this.lifecycleStatus = "WORKING";
        this.revisedCnt = 0;
        this.present = 1;
        this.project = project;
    }

    public void setPresent(Integer present) {
        //초기 값은 1(진행 중인 아이)
        this.present = present;
    }

    public void setRevisedCnt(Integer revisedCnt) {
        this.revisedCnt = revisedCnt;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    /**
     * 일반 라우트
     * @param req
     * @param routeProductRepository
     * @return
     */

    // route product 가 update 될 때마다 route Ordering 도 적절하게 업데이트 처리 해줘야 함

    public RouteOrderingUpdateRequest update(
            RouteOrderingUpdateRequest req,
            RouteProductRepository routeProductRepository,
            RouteOrderingRepository routeOrderingRepository,
            ProjectRepository projectRepository,
            ProjectService projectService

    ) {
        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(this);

        //이미 승인 완료됐을 시에는 더이상 승인이 불가능해 에러 던지기
        if(this.present==routeProductList.size()){
            throw new UpdateImpossibleException();
        }


        /**
         * update에서 받은 코멘트를 현재 진행중인 routeProduct에 set
         */
        //지금 애는 패스
        //내 앞에 완료됐던 애는 pass로 바꿔주기
        routeProductList.get(this.present-1).setPassed(true);

        if(this.present<routeProductList.size()-1) {
            //지금 들어온 코멘트는 현재 애 다음에
            routeProductList.get(this.present).setComment(req.getComment());

            // 지금 업데이트되는 라우트 프로덕트의 타입이 create 라면
            if(routeProductList.get(this.present).getType().getName().equals("CREATE")
                    || routeProductList.get(this.present).getType().getName().equals("REQUEST")){

                // 모듈이 프로젝트
                if(routeProductList.get(this.present).getType().getModule().equals("PROJECT")){
                    this.getProject().setTempsave(false); //라우트 만든 순간 임시저장 다시 거짓으로
                }
            }

        }else{
            routeProductList.get(this.present).setComment(req.getComment());
            //만약 present가 size() 가 됐다면 다 왔다는 거다.
            System.out.println(" ========================== route complete ==========================  ");
            this.lifecycleStatus = "COMPLETE";

            RouteOrdering routeOrdering = routeProductList.get(this.present).getRouteOrdering();

            // release 처리

            // 만약 지금 PROCESS 끝난 것이 RELEASE 라면 => 그 release 에 딸린
            // 아이템 혹은 CO 의 최신 ROUTE ORDERING 의 상태는 RELEASE
//
//            if(routeProductList.get(this.present).getType().getModule().equals("RELEASE")){
//
//                // (1) RELEASE 에 딸린 게 new item  라면
//                if(routeOrdering.getRelease().getNewItem()!=null){
//
//                    for(NewItem newItemChk : routeOrdering.getRelease().getNewItem()){
//                        if(routeOrderingRepository.findByNewItemOrderByIdAsc
//                                (newItemChk).size()>0) {
//
//                            RouteOrdering routeOrderingToRelease =
//                                    routeOrderingRepository.findByNewItemOrderByIdAsc(newItemChk)
//                                            .get(
//                                                    routeOrderingRepository
//                                                            .findByNewItemOrderByIdAsc
//                                                                    (newItemChk).size()-1
//                                            );
//                            // 각 아이템의 라우트 오더링을 release 로
//                            routeOrderingToRelease.updateLifeCycleToRelease();
//
//                        }
//                    }
//
//                    updateLifeCycleToRelease();
//
//                }
//
//
//                // (2) RELEASE 에 딸린 게 CO 라면
//                else if(routeOrdering.getRelease().getChangeOrder()!=null){
//
//                    for(CoNewItem coNewItem : routeOrdering.getRelease().getChangeOrder().getCoNewItems()){
//                        if(routeOrderingRepository.findByNewItemOrderByIdAsc(coNewItem.getNewItem()).size()>0) {
//                            RouteOrdering routeOrderingToRelease =
//                                    routeOrderingRepository.findByNewItemOrderByIdAsc(coNewItem.getNewItem())
//                                            .get(
//                                                    routeOrderingRepository
//                                                            .findByNewItemOrderByIdAsc
//                                                                    (coNewItem.getNewItem()).size()-1
//                                            );
//
//                            routeOrderingToRelease.updateLifeCycleToRelease();
//
//                        }
//                    }
//
//                    updateLifeCycleToRelease();
//                }
//
//            }
            ///////////////////////////////////////////////

//            /** #####경우 1 ) item revise route ordering 이었을 때
//             * 1인 경우는 item revise route ordering 이라는 뜻
//             */
//            if(routeProductList.get(this.present).getRouteOrdering().getRevisedCnt()==1){
//                //지금 승인된 라우트가 revise 로 인해 새로 생긴 아이템이라면
//                routeOrdering.setRevisedCnt(0);
//                //0710 revise 로 생긴 route ordering 이었다면 다시 0으로 복구;
//
//                if(routeOrdering.getNewItem()==null){
//                    //revise 인데도 안 묶여있으면 뭔가 잘못됐어,
//                    throw new ItemNotFoundException();//에러 던지기
//                }
//
//                NewItem targetOldRevisedItem = newItemRepository.
//                        findById(routeOrdering.getNewItem().getReviseTargetId()).orElseThrow(ItemNotFoundException::new);
//
//                NewItem completedNewItem = routeOrdering.getNewItem();
//                completedNewItem.setReviseTargetId(null); //끝나면 null 로 관계 끊어주기
//
//                if (targetOldRevisedItem.isRevise_progress()) {
//                    targetOldRevisedItem.setRevise_progress(false);
//                    //0712 아기의 target route 가 revise progress 가 진행 중이라면 라우트 complete 될 때 false 로 갱신
//                }
//
//                if(coNewItemRepository.findByNewItemOrderByCreatedAtAsc(targetOldRevisedItem).size()>0) {
//                    // (1) 지금 revise 완료 된 아이템의 CO 를 검사하기 위해 check co 찾기
//                    System.out.println("(1) 지금 revise 완료 된 아이템의 CO 를 검사하기 위해 check co 찾기");
//                    ChangeOrder checkCo =
//                            coNewItemRepository.findByNewItemOrderByCreatedAtAsc(targetOldRevisedItem).get(
//                                            coNewItemRepository.findByNewItemOrderByCreatedAtAsc(targetOldRevisedItem).size()-1
//                                    )//가장 최근에 맺어진 co-new item 관계 중 가장 최신 아이의 co를 검사하기
//                                    .getChangeOrder();
//
//                    System.out.println(checkCo.getId() + "가 co의 아이디 값을 가리킨다. ");
//
//                    // (2) check co 의 affected item 리스트
//                    System.out.println("(2) check co 의 affected item 리스트");
//                    List<CoNewItem> coNewItemsOfChkCo = checkCo.getCoNewItems();
//                    List<NewItem> affectedItemOfChkCo = coNewItemsOfChkCo.stream().map(
//                            i->i.getNewItem()
//                    ).collect(Collectors.toList());
//
//                    System.out.println(affectedItemOfChkCo.size()+"는 affecte item 의 갯수 길이 ");
//
//                    // (3) checkCo의 routeOrdering 찾아오기
//                    System.out.println("(3) checkCo의 routeOrdering 찾아오기");
//                    RouteOrdering routeOrderingOfChkCo =
//                            routeOrderingRepository.findByChangeOrderOrderByIdAsc(checkCo).get(
//                                    routeOrderingRepository.findByChangeOrderOrderByIdAsc(checkCo).size()-1
//                            );
//
//                    // (4) affected item 이 모두 revise 완료된다면 update route
//                    System.out.println("(4) affected item 이 모두 revise 완료된다면 update route");
//                    // revise complete
//                    System.out.println(newItemService.checkReviseCompleted(affectedItemOfChkCo));
//                    System.out.println("위가 revise 완료됐는지 여부를 알려주지이이");
//                    if(newItemService.checkReviseCompleted(affectedItemOfChkCo)){
//                        routeOrderingOfChkCo.CoUpdate(routeProductRepository);
//                    }
//
//                }
//
//                //throw new UpdateImpossibleException();
//                // 0710 : 이 아이템과 엮인 아이들 (CHILDREN , PARENT )들의 REVISION +=1 진행 !
//                // 대상 아이템들은 이미 각각 아이템 리뷰 / 프로젝트 링크할 때 REVISION+1 당함
//                newItemService.revisionUpdateAllChildrenAndParentItem(routeOrdering.getNewItem());
//
//            }
//
//
//            /** #####경우 2 ) item revise route ordering 이었을 때
//             * 2인 경우는 document revise route ordering 이라는 뜻
//             */
//            else if(routeProductList.get(this.present).getRouteOrdering().getRevisedCnt()==2){
//
//                Document oldDocument
//                        = routeProductList.get(0)
//                        .getRouteOrdering()
//                        .getDocument()
//                        .getReviseTargetDoc()
//                        ;
//
//                oldDocument.reviseProgressFalse();
//
//
//            }


        }

        /**
         * 라우트프로덕트 맨 마지막 인덱스 값 찾기 위한 변수
         */
        Integer lastIndexofRouteProduct =
                routeProductList.size()-1;

        /**
         * 승인, 거부 시 갱신
         * 서비스 단에서 routeProduct 승인 혹은 거절 후
         * 라우트 업데이트 호출해서 present 갱신해줄거야
         */
        //present 를 다음 진행될 애로 갱신해주기
        if(this.present<routeProductList.size()) {
            this.present = this.present + 1;
        }



        return req;
    }

    public void updateToComplete() {
        this.lifecycleStatus="COMPLETE";
    }

    public void updateLifeCycleToRelease() {
        this.lifecycleStatus="RELEASE";
    }


    public List<RouteProduct> rejectUpdate(

            Long id,
            String rejectedComment,
            Integer rejectedIndex,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository
//            ,
//            DevelopmentBomRepository developmentBomRepository

    ) {
        /**
         * 현재 라우트에 딸린 라우트 생산물들
         */
        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(this);

        if(rejectedIndex > this.present || routeProductList.get(rejectedIndex).isDisabled()){
            throw new RejectImpossibleException();
        }

        if(this.present<=routeProductList.size()-1) { //05-23 type 1 오류의 원인이었습니다.
            this.present = this.present + 1;
        }

        /**
         * 라우트프로덕트 맨 마지막 인덱스 값 찾기 위한 변수
         */
        Integer lastIndexofRouteProduct =
                routeProductList.size()-1;

        /**
         * 거절 전의 아이 + 거절주체인 차례 아이도 passed = true로 해주기
         * (수행 된거니깐)
         */

        routeProductList.get(this.present - 2).setPassed(true);
        routeProductList.get(this.present - 1).setPassed(true);


        /**
         * 기존 애들 중에서 passed 가 false 인 애들의 show 는 false 로 변경해주기
         */
        for(RouteProduct routeProductshow : routeProductList){
            if(!routeProductshow.isPassed()){
                routeProductshow.setShow(false);
                //아직 지나지 않은 애들은 화면에서 없애주기
            }
            if(routeProductshow.getSequence()>rejectedIndex){
                //거부당한 인덱스보다 큰 애들은 쭉~ 무효화처리 (reject 대상이 되지 못해)
                routeProductshow.setDisabled(true);
            }
        }

        // 거부된 라우트 프로덕트의 라우트 타입 검사
        switch(routeProductList.get(rejectedIndex).getType().getId().toString()) {

            case "1":
                this.getProject().setTempsave(true);
                this.getProject().setReadonly(false);
                break;
        }

        /**
         * 거부됐던 애의 reject 는 true 로 갱신하고 (거부받은자에게 알림용)
         * 거부한 차례 애의 comment엔 거부 코멘트(req에서 받은 것) set
         */
        routeProductList.get(rejectedIndex).setRejected(true);
        routeProductList.get(rejectedIndex).setDisabled(true);
        routeProductList.get(this.present-1).setComment(rejectedComment);
        routeProductList.get(this.present-1).updateRefusal(rejectedIndex); //06-01 : reject 된 seq을 전달

        /**
         * 거부된 라우트 하나 먼저 복제
         *         // 특히 리젝트 된 아이 복제 대상애는 rejected= 1 로 설정해주고,
         *         // comment들 싹 다 초기화해주고
         */

        Integer seq = this.present;
        RouteProduct rejectedRouteProduct =
                new RouteProduct(
                        seq,
                        routeProductList.get(rejectedIndex).getOrigin_seq(),
                        routeProductList.get(rejectedIndex).getRoute_name(),

                        routeProductList.get(rejectedIndex).getType(),
                        "default",
                        false,
                        false,
                        true, //이전에 거절 당해서 만들어진 애라는 뜻
                        true,
                        false,
                        -1, //0527 - 거절당한 것, 거절자는 아니다
                        routeProductList.get(rejectedIndex)
                                .getMembers().stream().map(
                                        RouteProductMember::getMember
                                )
                                .collect(toList()),
                        routeProductList.get(rejectedIndex).getRouteOrdering(),
                        routeProductList.get(rejectedIndex).getProject()
                );

        /**
         * 라우트의 present 도 이 아이의 인덱스 값으로 변경시켜주자. (이건 서비스에서)
         */

        /**
         * 거부된 라우트 이후부터t(r
         * 쭉~~~ 끝 인덱스까지 슬라이싱 해서
         * rejectedList로 만들기
         * (얘네 복제할거임)
         */
        List <RouteProduct> duplicateRouteProductList =
                routeProductList.subList(
                        rejectedIndex+1,
                        lastIndexofRouteProduct+1
                        //sublist는 하나 0,3이라면 0,1,2 인덱스 복사
                );

        System.out.println("지금 상황에선 거절대상 이후로 복제돼야 하는 갯수");
        System.out.println(duplicateRouteProductList.size());

        AtomicReference<Integer> sequence = new AtomicReference<>(rejectedRouteProduct.getSequence());
        List<RouteProduct> duplicateList = duplicateRouteProductList.stream().map(
                i ->
                        new RouteProduct(
                                //자기 복제대상보다 1이 더 커야해 다들

                                sequence.updateAndGet(v -> v + 1),

                                i.getOrigin_seq(),

                                i.getRoute_name(),

                                i.getType(),
                                "default",
                                false, //passed
                                false, //rejected
                                false, //preRejected 돼서 만들어진 것이 아니니깐
                                true,
                                false,
                                -1,
                                i.getMembers().stream().map(
                                        m -> m.getMember()
                                ).collect(toList()),
                                i.getRouteOrdering()
                        )
        ).collect(
                toList()
        );
        List<RouteProduct> allRouteProductList =
                routeProductRepository.findAllByRouteOrdering(this);

        Integer lastIndex = allRouteProductList.size()-1;
        List<RouteProduct> addedRouteProductList =
                new ArrayList<>();

        //거절된 애 추가 중
        addedRouteProductList.add(rejectedRouteProduct);

        addedRouteProductList.addAll(duplicateList);

        return addedRouteProductList;
    }
}

