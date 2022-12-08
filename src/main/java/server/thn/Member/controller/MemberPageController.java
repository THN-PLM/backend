package server.thn.Member.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.thn.Member.dto.MemberDto;
import server.thn.Member.service.member.MemberService;

import javax.transaction.Transactional;

@Transactional
@RestController
@RequiredArgsConstructor
public class MemberPageController{

    private final MemberService memberService;

    @CrossOrigin(origins = "https://thn-plm.th-net.co.kr")
    @GetMapping("/members/page")
    //@ApiOperation(value = "member page GET", notes = "멤버 리스트 페이징 GET")
    public Page<MemberDto> paging(@PageableDefault(size=5)
                                  @SortDefault.SortDefaults({
                                          @SortDefault(
                                                  sort = "createdAt",
                                                  direction = Sort.Direction.DESC)
                                  })
                                          Pageable pageRequest,
                                  //@RequestParam(value = "keyWord",  required=false) String keyWord,
                                  @RequestParam(value ="name", required=false) String name) {

        return memberService.returnCustomPagingProject(name, pageRequest);
    }

}

