package server.thn.Project.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.thn.Aop.AssignMemberId;
import server.thn.Common.dto.MemberIdReq;
import server.thn.Project.dto.ProjectDto;
import server.thn.Project.service.ProjectService;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;

@Transactional
@RestController
@RequiredArgsConstructor
public class ProjectPageController {
    private final ProjectService projectService;

    /**
     * 프로젝트 모듈에서의 프로젝트 리스트(내가 만든 프로젝트들maybe..?)
     */
    @CrossOrigin(origins = "https://eci-plm.kro.kr")
    @GetMapping("/project/page")
    public Page<ProjectDto> pagingProject(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(
                            sort = "createdAt",
                            direction = Sort.Direction.DESC)
            })
                    Pageable pageRequest,
            @RequestParam(value = "name", required = false) String name) throws UnsupportedEncodingException {
        return projectService.returnPagingProject(name, pageRequest);
    }


    // admin 만 보이는 프로젝트

    @CrossOrigin(origins = "https://eci-plm.kro.kr")
    @GetMapping("/project/management")
    @AssignMemberId
    public Page<ProjectDto> pagingManagingProject(
            @PageableDefault(size=5)
            @SortDefault.SortDefaults({
                    @SortDefault(
                            sort = "createdAt",
                            direction = Sort.Direction.DESC)
            })

            Pageable pageRequest,
            MemberIdReq req,
            @RequestParam(value = "name", required = false) String name) throws UnsupportedEncodingException {

        if(name==null) {
            return projectService.readManagedProject(pageRequest, req);

        }else{
            return projectService.returnPagingProject(name , pageRequest);
        }
    }
}