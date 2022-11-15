package server.thn.Project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.thn.Aop.AssignMemberId;
import server.thn.Aop.AssignModifierId;
import server.thn.Common.dto.response.Response;
import server.thn.File.repository.AttachmentTagRepository;
import server.thn.File.service.FileService;
import server.thn.Member.repository.MemberRepository;
import server.thn.Project.dto.ProjectCreateRequest;
import server.thn.Project.repository.ProjectAttachmentRepository;
import server.thn.Project.repository.ProjectRepository;
import server.thn.Project.repository.ProjectTypeRepository;
import server.thn.Project.repository.buyer.BuyerOrganizationRepository;
import server.thn.Project.repository.carType.CarTypeRepository;
import server.thn.Project.repository.produceOrg.ProduceOrganizationRepository;
import server.thn.Project.service.ProjectService;
import server.thn.Route.repository.RouteOrderingRepository;
import server.thn.Route.repository.RouteProductRepository;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://localhost:3000")
public class ProjectController {

    private final MemberRepository memberRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ProjectRepository projectRepository;
    private final BuyerOrganizationRepository buyerOrganizationRepository;
    private final ProduceOrganizationRepository produceOrganizationRepository;
    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final CarTypeRepository carTypeRepository;
    private final AttachmentTagRepository attachmentTagRepository;;
    private final ProjectAttachmentRepository projectAttachmentRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    private final ProjectService projectService;

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/project/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    @AssignModifierId
    public Response tempCreate(
            @Valid @ModelAttribute
                    ProjectCreateRequest req
    ) {

        return Response.success(

                projectService.tempCreate(req));
    }


    /**
     * 프로젝트 생성 (찐 저장)
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/project")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    @AssignModifierId
    public Response create(
            @Valid @ModelAttribute
                    ProjectCreateRequest req
    ) {

        return Response.success(

                projectService.create(req));
    }
//
//    /**
//     * 특정 프로젝트 수정
//     *
//     * @param id
//     * @param req
//     * @return
//     */
//    @CrossOrigin(origins = "https://localhost:3000")
//    @PutMapping("/project/temp/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    @AssignModifierId //수정자 추가
//    public Response update(
//            @PathVariable Long id,
//            @Valid @ModelAttribute
//                    ProjectUpdateRequest req
//    ) {
//
//        return Response.success(projectService.update(id, req));
//    }
//
//    @CrossOrigin(origins = "https://localhost:3000")
//    @PutMapping("/project/temp/end/{id}")
//    @ResponseStatus(HttpStatus.CREATED)
//    @AssignModifierId //0605 : 수정 시에는 글쓴이 아디 주입 아니고, 수정자 아이디 주입
//    public Response tempEnd(
//            @PathVariable Long id,
//            @Valid @ModelAttribute
//                    ProjectUpdateRequest req
//    ) {
//
//        return Response.success(
//                projectService.tempEnd(id, req)
//        );
//    }
//
//    @CrossOrigin(origins = "https://localhost:3000")
//    @DeleteMapping("/project/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Response delete(@PathVariable Long id) {
//        projectService.delete(id);
//        return Response.success();
//    }
//
    @GetMapping("/project/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(projectService.read(id));
    }

    // delete , drop , pending API

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project-delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId //수정자 추가
    public Response deleted(
            @PathVariable Long id
    ) {

        return Response.success(projectService.deleted(id));
    }

    // project/drop/{id}

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project-drop/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId //수정자 추가
    public Response drop(
            @PathVariable Long id
    ) {

        return Response.success(projectService.drop(id ));
    }

    // project/pending/{id}

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project-pending/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId //수정자 추가
    public Response pending(
            @PathVariable Long id
    ) {

        return Response.success(projectService.pending(id ));
    }


}
