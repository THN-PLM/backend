package server.thn.Member.controller;

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
@CrossOrigin(origins = "https://localhost:3000")
public class RoleController {

    private final RoleService roleService;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll() {
        return Response.success(roleService.readAll());
    }


}
