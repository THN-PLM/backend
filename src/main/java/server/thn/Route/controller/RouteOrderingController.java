package server.thn.Route.controller;

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


    @GetMapping("/route")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(
            @Valid RouteOrderingReadCondition cond
    ) {
        return Response.success(newRouteService.readAll(cond));
    }

    @PostMapping("/route/project")
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
    @CrossOrigin(origins = "https://eci-plm.kro.kr")
    @GetMapping("/routeByProj/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response readRouteByItem(@PathVariable Long id) {
        return Response.success(
                newRouteService.readRouteByProj(id)
        );
    }

    @PutMapping("/approveRoute/{id}")
    @ResponseStatus(HttpStatus.OK)
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
    public Response possibleRejectRouteProductId(@PathVariable Long id) {
        return Response.success(
                newRouteService.rejectPossible(id)
        );
    }

    @GetMapping("/route/members/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    public Response membersFromBeforeRoute(@PathVariable Long routeId) {
        return Response.success(
                newRouteService.memberRead(routeId)
        );
    }

    @PutMapping("/addMember")
    @ResponseStatus(HttpStatus.OK)
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