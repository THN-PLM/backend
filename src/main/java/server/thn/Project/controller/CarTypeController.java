package server.thn.Project.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.thn.Common.dto.response.Response;
import server.thn.Project.dto.carType.CarTypeReadCondition;
import server.thn.Project.service.CarTypeService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://kthn-plm.kro.kr")
public class CarTypeController {
    private final CarTypeService carTypeService;

    @CrossOrigin(origins = "https://kthn-plm.kro.kr")
    @GetMapping("/carTypeId")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "car list get", notes = "car list get")
    public Response readAll(@Valid CarTypeReadCondition cond) {
        return Response.success(
                carTypeService.
                        readAll(cond));
    }

//    @PostMapping("/carTypeId")
//    @ResponseStatus(HttpStatus.OK)
//    public Response create(@Valid IdNameDto cond) {
//        return Response.success(
//                carTypeService.
//                        create(cond));
//    }

    @CrossOrigin(origins = "https://kthn-plm.kro.kr")
    @DeleteMapping("/carType/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "car delete", notes = "car delete")
    public Response delete(@PathVariable Long id) {
        carTypeService.delete(id);
        return Response.success();
    }

}
