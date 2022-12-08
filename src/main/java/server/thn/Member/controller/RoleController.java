package server.thn.Member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import server.thn.Common.dto.response.Response;
import server.thn.Member.service.RoleService;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://thn-plm.th-net.co.kr")
public class RoleController {

    private final RoleService roleService;

    @CrossOrigin(origins = "https://thn-plm.th-net.co.kr")
    @GetMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "멤버 role GET", notes = "역할 리스트 불러오기")
    public Response readAll() {
        return Response.success(roleService.readAll());
    }


}
