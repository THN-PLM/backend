package server.thn.Route.dto.routeOrdering;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Project.entity.Project;
import server.thn.Project.entity.ProjectType;
import server.thn.Project.entity.ProjectTypeEnum;
import server.thn.Project.exception.ProjectNotFoundException;
import server.thn.Project.repository.ProjectRepository;
import server.thn.Route.entity.RouteOrdering;
import server.thn.Route.entity.RoutePreset;
import server.thn.Route.repository.RouteTypeRepository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteOrderingCreateRequest {

    @NotNull(message = "아이템 아이디를 입력해주세요")
    @Positive(message = "올바른 아이템 아이디를 입력해주세요")
    private Long itemId;

    @Null // 첫번째 멤버 아이디는 무조건 itemId 작성자랑 동일 인물
    private Long memberId;

    private ArrayList<ArrayList<Long>> memberIds;

    private String requestComment;

    public static RouteOrdering toEntity(
            RouteOrderingCreateRequest req,
            ProjectRepository projectRepository,
            RoutePreset routePreset,
            RouteTypeRepository routeTypeRepository
            //ItemType itemType
    ){
        Project targetProj = projectRepository.findById(req.itemId).orElseThrow(ProjectNotFoundException::new);

        List<String> typeList = new ArrayList<>();

        //아이템 타입에따라서 라우트 타입이 선택된다.

        Integer routeType =
                ProjectTypeEnum.valueOf(
                        targetProj.getProjectType().getProjectType().name()
                ).label();

        List routeProduct = List.of((routePreset.projectRouteName[routeType]));

        for(Object type : routeProduct){
            typeList.add(type.toString());
        }


        return new RouteOrdering(
                typeList.toString(),
                projectRepository.findById(req.itemId)
                        .orElseThrow(ProjectNotFoundException::new)
        );
    }

//
//    public static RouteOrdering toRevisedItemRouteOrderingEntity(
//            RouteOrderingCreateRequest req,
//            NewItemRepository newItemRepository,
//            RoutePreset routePreset,
//            RouteTypeRepository routeTypeRepository
//            //ItemType itemType
//    ){
//        NewItem targetItem = newItemRepository.findById(req.itemId).orElseThrow(ItemNotFoundException::new);
//
//        List<String> typeList = new ArrayList<>();
//
//        //아이템 타입에따라서 라우트 타입이 선택된다.
//
//        // TODO 라벨 아니고 ITEM.ROUTE_TYPE.ID 로 선택해준다
//        Integer routeType =
//                ItemType.valueOf(
//                        targetItem.getItemTypes().getItemType().name()
//                ).label();
//
//        List routeProduct = List.of((routePreset.itemRouteName[routeType]));
//
//        for(Object type : routeProduct){
//            typeList.add(type.toString());
//
//        }
//
//// 0712
//        return new RouteOrdering(
//                1, //revised_cnt
//                typeList.toString(),
//                newItemRepository.findById(req.itemId)
//                        .orElseThrow(ItemNotFoundException::new)
//        );
//    }
//
//    public static RouteOrdering toCrEntity(
//            RouteOrderingCreateRequest req,
//            RoutePreset routePreset,
//            ChangeRequestRepository changeRequestRepository,
//            RouteTypeRepository routeTypeRepository
//    ){
//
//        List<String> typeList = new ArrayList<>();
//
//        List routeProduct = List.of((routePreset.CRRouteName[0]));
//
//        for(Object type : routeProduct){
//            typeList.add(type.toString());
//
//        }
//
//        return new RouteOrdering(
//                typeList.toString(),
//                changeRequestRepository.findById(req.itemId)
//                        .orElseThrow(MemberNotFoundException::new)
//        );
//    }
//
//
//    public static RouteOrdering toCoEntity(
//            RouteOrderingCreateRequest req,
//            RoutePreset routePreset,
//            ChangeOrderRepository changeOrderRepository,
//            RouteTypeRepository routeTypeRepository
//    ){
//
//        List<String> typeList = new ArrayList<>();
//
//        List routeProduct = List.of((routePreset.CORouteName[0]));
//
//        for(Object type : routeProduct){
//            typeList.add(type.toString());
//
//        }
//
//        return new RouteOrdering(
//                typeList.toString(),
//                changeOrderRepository.findById(req.itemId)
//                        .orElseThrow(CoNotFoundException::new)
//        );
//    }
//
//
//    public static RouteOrdering toReleaseEntity(
//            RouteOrderingCreateRequest req,
//            RoutePreset routePreset,
//            ReleaseRepository releaseRepository
//    ){
//
//        List<String> typeList = new ArrayList<>();
//
//        List routeProduct = List.of((routePreset.RELEASERouteName[0]));
//
//        for(Object type : routeProduct){
//            typeList.add(type.toString());
//
//        }
//
//        return new RouteOrdering(
//                typeList.toString(),
//                releaseRepository.findById(req.itemId)
//                        .orElseThrow(ReleaseNotFoundException::new)
//        );
//    }
//
//
//    /**
//     * DOCUMENT
//     * @param req
//     * @param routePreset
//     * @param documentRepository
//     * @return
//     */
//    public static RouteOrdering toDocumentEntity(
//            RouteOrderingCreateRequest req,
//            RoutePreset routePreset,
//            DocumentRepository documentRepository
//    ){
//
//        List<String> typeList = new ArrayList<>();
//
//        List routeProduct = List.of((routePreset.DOCRouteName[0]));
//
//        for(Object type : routeProduct){
//            typeList.add(type.toString());
//
//        }
//
//        return new RouteOrdering(
//                typeList.toString(),
//                documentRepository.findById(req.itemId)
//                        .orElseThrow(DocumentNotFoundException::new)
//        );
//    }
//
//
//    public static RouteOrdering toRevisedDocumentEntity(
//            RouteOrderingCreateRequest req,
//            RoutePreset routePreset,
//            DocumentRepository documentRepository
//    ){
//
//        List<String> typeList = new ArrayList<>();
//
//        List routeProduct = List.of((routePreset.DOCRouteName[0]));
//
//        for(Object type : routeProduct){
//            typeList.add(type.toString());
//
//        }
//
//        return new RouteOrdering(
//                2,
//                typeList.toString(),
//                documentRepository.findById(req.itemId)
//                        .orElseThrow(DocumentNotFoundException::new)
//        );
//    }


    public static RouteOrdering toProjectEntity(
            RouteOrderingCreateRequest req,
            RoutePreset routePreset,
            ProjectRepository projectRepository
    ){

        List<String> typeList = new ArrayList<>();

        List routeProduct = List.of((routePreset.projectRouteName[0]));

        for(Object type : routeProduct){
            typeList.add(type.toString());

        }

        return new RouteOrdering(
                typeList.toString(),
                projectRepository.findById(req.itemId)
                        .orElseThrow(ProjectNotFoundException::new)
        );
    }

}
