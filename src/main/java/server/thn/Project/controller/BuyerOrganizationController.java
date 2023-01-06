package server.thn.Project.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.thn.Common.dto.response.Response;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.service.BuyerOrganizationService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://kthn-test.o-r.kr:441")
public class BuyerOrganizationController {

    private final BuyerOrganizationService buyerOrganizationService;

    @CrossOrigin(origins = "https://kthn-test.o-r.kr:441")
    @GetMapping("/buyerOrganization")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "발주처 list get", notes = "발주처 list get")
    public Response readAll(@Valid ReadCondition cond) {
        return Response.success(
                buyerOrganizationService.
                        readAll(cond));
    }

    @CrossOrigin(origins = "https://kthn-test.o-r.kr:441")
    @DeleteMapping("/buyerOrganization/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "발주처 list 삭제", notes = "발주처 삭제")
    public Response delete(@PathVariable Long id) {
        buyerOrganizationService.delete(id);
        return Response.success();
    }



    @CrossOrigin(origins = "https://kthn-test.o-r.kr:441")
    @GetMapping("/classification/buyerOrganization")
    @ResponseStatus(HttpStatus.OK)
    public Response readClassification1All() {
        return Response.success(
                buyerOrganizationService.
                        readAllBuyerOrganizationClassification1());
    }


}
