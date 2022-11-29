package server.thn.Project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import server.thn.Aop.AssignMemberId;
import server.thn.Aop.AssignModifierId;
import server.thn.Common.dto.response.Response;
import server.thn.Project.dto.ProjectCreateRequest;
import server.thn.Project.dto.ProjectUpdateRequest;import server.thn.Project.service.ProjectService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    @Value("${default.image.address}")
    private String defaultImageAddress;

    private final ProjectService projectService;

    @CrossOrigin(origins = "http://localhost:3000")
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
    @CrossOrigin(origins = "http://localhost:3000")
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
    /**
     * 특정 프로젝트 수정
     *
     * @param id
     * @param req
     * @return
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project/temp/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId //수정자 추가
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    ProjectUpdateRequest req
    ) {

        return Response.success(projectService.update(id, req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project/temp/end/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId
    public Response tempEnd(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    ProjectUpdateRequest req
    ) {
        return Response.success(
                projectService.tempEnd(id, req)
        );
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @DeleteMapping("/project/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        projectService.delete(id);
        return Response.success();
    }

    @GetMapping("/project/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(projectService.read(id));
    }

    // delete , drop , pending API

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project-delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleted(
            @PathVariable Long id
    ) {
        return Response.success(projectService.deleted(id));
    }

    // project/drop/{id}

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project-drop/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response drop(
            @PathVariable Long id
    ) {
        return Response.success(projectService.drop(id ));
    }

    // project/pending/{id}
    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project-pending/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response pending(
            @PathVariable Long id
    ) {
        return Response.success(projectService.pending(id ));
    }


}
