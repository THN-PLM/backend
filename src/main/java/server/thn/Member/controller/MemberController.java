package server.thn.Member.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.thn.Common.dto.response.Response;
import server.thn.Member.dto.MemberReadCondition;
import server.thn.Member.service.member.MemberService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://thn-plm.th-net.co.kr")
public class MemberController {

    private final MemberService memberService;

    @CrossOrigin(origins = "https://thn-plm.th-net.co.kr")
    @GetMapping("/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(memberService.read(id));
    }

    @CrossOrigin(origins = "https://thn-plm.th-net.co.kr")
    @DeleteMapping("/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        memberService.delete(id);
        return Response.success();
    }

    @CrossOrigin(origins = "https://thn-plm.th-net.co.kr")
    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid MemberReadCondition cond) {
        return Response.success(memberService.readAll(cond));
    }

}
