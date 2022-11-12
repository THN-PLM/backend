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
import server.thn.Common.page.CustomPageImpl;
import server.thn.Member.dto.MemberDto;
import server.thn.Member.entity.Member;
import server.thn.Member.service.member.MemberService;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification;
import server.thn.Member.exception.ClassificationNotFoundException;
import server.thn.Member.repository.MemberRepository;
import server.thn.Project.repository.classification.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequiredArgsConstructor
public class MemberPageController{

    private final MemberService memberService;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/members/page")
    @ApiOperation(value = "member page GET", notes = "멤버 리스트 페이징 GET")
    public Page<MemberDto> paging(@PageableDefault(size=5)
                                  @SortDefault.SortDefaults({
                                          @SortDefault(
                                                  sort = "createdAt",
                                                  direction = Sort.Direction.DESC)
                                  })
                                          Pageable pageRequest,
                                  @RequestParam(value = "keyWord",  required=false) String keyWord,
                                  @RequestParam(value ="name", required=false) String name) {

        return memberService.returnCustomPagingProject(name, pageRequest);
    }

}

