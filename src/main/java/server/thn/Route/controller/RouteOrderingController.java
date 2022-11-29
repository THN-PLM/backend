package server.thn.Route.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.thn.Aop.AssignMemberId;
import server.thn.Common.dto.response.Response;
import server.thn.Route.dto.routeOrdering.*;
import server.thn.Route.dto.routeProduct.RouteProductResponsiblePassDto;
import server.thn.Route.service.RouteOrderingService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000")
public class RouteOrderingController {
    private final RouteOrderingService newRouteService;


    @PostMapping("/route/proj")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createProjRoutes(
            @Valid RouteOrderingCreateRequest req) {

        return Response.success(
                newRouteService.createProjectRoute(req)
        );

    }

    @GetMapping("/route/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "route GET ", notes = "{id} 에 해당하는 라우트 데려오기}")
    public Response read(@PathVariable Long id) {
        return Response.success(
                newRouteService.read(id)
        );
    }

    /**
     * 아이템 타입 넘겨주면
     * @param id
     * @return
     */

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/routeByProj/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "프로젝트 타입에 따른 라우트 데려오기", notes = "양산/선형에 따라서")
    public Response readRouteByItem(@PathVariable Long id) {
        return Response.success(
                newRouteService.readRouteByProj(id)
        );
    }

    @PutMapping("/approveRoute/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "route 승인", notes = "route 승인")
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute RouteOrderingUpdateRequest req) {

        RouteUpdateResponse routeUpdateResponse = newRouteService.approveUpdate(id, req);

        return Response.success(
                routeUpdateResponse
        );
    }

    @PutMapping("/rejectRoute/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "route 거절", notes = "route 거절에 따른 작업 처리 ")
    public Response rejectUpdate(
            @PathVariable Long id,
            @Valid @ModelAttribute RouteOrderingRejectRequest req) {
        {
            return Response.success(
                    newRouteService.rejectUpdate(
                            id, req.getComment(), req.getRejectedSequence()
                    )
            );
        }
    }

    /**
     * 현시점에서 거절가능한 라우트프로덕트 아이디
     * @param id (라우트오더링)
     * @return
     */
    @GetMapping("/route/reject-possible/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "거절 가능 route list", notes = "내 앞에 애들 중 거절 가능한 route")
    public Response possibleRejectRouteProductId(@PathVariable Long id) {
        return Response.success(
                newRouteService.rejectPossible(id)
        );
    }

    @GetMapping("/route/members/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "route에 딸린 멤버들", notes = "route 멤버 리스트 get ")
    public Response membersFromBeforeRoute(@PathVariable Long routeId) {
        return Response.success(
                newRouteService.memberRead(routeId)
        );
    }

    @PutMapping("/addMember")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "route 멤버(담당자) 추가", notes = "관리자 페이지서 책임 추가 ")
    public Response addMember(
            @Valid @ModelAttribute RouteProductResponsiblePassDto req) {
        {
            return Response.success(
                    newRouteService.routeProductAddResponsibilityMember(
                            req
                    )
            );
        }
    }



}