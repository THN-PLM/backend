package server.thn.Project.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.thn.Common.dto.response.Response;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.service.ProduceOrganizationService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://localhost:3000")
public class ProduceOrganizationController {

    private final ProduceOrganizationService produceOrganizationService;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/produceOrganizationId")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "생산조직 list get", notes = "생산조직 list get")
    public Response readAll(@Valid ReadCondition cond) {
        return Response.success(
                produceOrganizationService.
                        readAll(cond));
    }

//    @PostMapping("/carTypeId")
//    @ResponseStatus(HttpStatus.OK)
//    public Response create(@Valid IdNameDto cond) {
//        return Response.success(
//                carTypeService.
//                        create(cond));
//    }

    @CrossOrigin(origins = "https://localhost:3000")
    @DeleteMapping("/produceOrganizationId/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "생산조직 list 삭제", notes = "생산조직 삭제")
    public Response delete(@PathVariable Long id) {
        produceOrganizationService.delete(id);
        return Response.success();
    }

}

