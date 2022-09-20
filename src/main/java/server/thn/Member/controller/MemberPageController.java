package server.thn.Member.controller;


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
import server.thn.Member.entity.teamClassification.TeamClassification;
import server.thn.Member.exception.ClassificationNotFoundException;
import server.thn.Member.repository.MemberRepository;
import server.thn.Member.repository.classification.*;
import server.thn.Member.service.member.MemberService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequiredArgsConstructor
public class MemberPageController{

    private final MemberRepository memberRepository;
    private final TeamClassificationRepository teamClassificationRepository;
    private final TeamClassification1Repository teamClassification1Repository;
    private final TeamClassification2Repository teamClassification2Repository;
    private final TeamClassification3Repository teamClassification3Repository;
    private final TeamClassification4Repository teamClassification4Repository;


    @Value("${default.image.address}")
    private String defaultImageAddress;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/members/page")
    public Page<MemberDto> paging(@PageableDefault(size=5)
                                  @SortDefault.SortDefaults({
                                          @SortDefault(
                                                  sort = "createdAt",
                                                  direction = Sort.Direction.DESC)
                                  })
                                          Pageable pageRequest,
                                  @RequestParam(value = "keyWord",  required=false) String keyWord,
                                  @RequestParam(value ="name", required=false) String name) {

        Page<Member> memList = null;
        long totalSize = 0;

        if(name==null && keyWord==null){
            totalSize+=memberRepository.findAll().size();
            memList = memberRepository.findAll(pageRequest);
        }else if(name!=null && keyWord==null){
            totalSize+=memberRepository.findByUsernameContainingIgnoreCase(name).size();
            memList = memberRepository.findByUsernameContainingIgnoreCase(name, pageRequest);
        }else if(name==null && keyWord!=null){

            String[] departId = (keyWord.split("/"));
            Long class4Id = Long.parseLong(departId[3]);
            Long class3Id = Long.parseLong(departId[2]);
            Long class2Id = Long.parseLong(departId[1]);
            Long class1Id = Long.parseLong(departId[0]);

            TeamClassification teamClassification = teamClassificationRepository.findByClassification1AndClassification2AndClassification3AndClassification4(
                    teamClassification1Repository.findById(class1Id).orElseThrow(ClassificationNotFoundException::new),
                    teamClassification2Repository.findById(class2Id).orElseThrow(ClassificationNotFoundException::new),
                    teamClassification3Repository.findById(class3Id).orElseThrow(ClassificationNotFoundException::new),
                    teamClassification4Repository.findById(class4Id).orElseThrow(ClassificationNotFoundException::new)
            );
            totalSize+=memberRepository.findByDepartment(teamClassification).size();
            memList = memberRepository.findByDepartment(teamClassification, pageRequest);
        }else{

            String[] departId = (keyWord.split("/"));
            Long class4Id = Long.parseLong(departId[3]);
            Long class3Id = Long.parseLong(departId[2]);
            Long class2Id = Long.parseLong(departId[1]);
            Long class1Id = Long.parseLong(departId[0]);

            TeamClassification teamClassification = teamClassificationRepository.findByClassification1AndClassification2AndClassification3AndClassification4(
                    teamClassification1Repository.findById(class1Id).orElseThrow(ClassificationNotFoundException::new),
                    teamClassification2Repository.findById(class2Id).orElseThrow(ClassificationNotFoundException::new),
                    teamClassification3Repository.findById(class3Id).orElseThrow(ClassificationNotFoundException::new),
                    teamClassification4Repository.findById(class4Id).orElseThrow(ClassificationNotFoundException::new)
            );

            totalSize+=memberRepository.findByUsernameContainingIgnoreCaseAndDepartment(
                    name, teamClassification
            ).size();
            memList = memberRepository.findByUsernameContainingIgnoreCaseAndDepartment(
                    name, teamClassification, pageRequest
            );

        }
        List<String> indexes = new ArrayList<>();
        indexes.add("Name");
        indexes.add("Department");
        indexes.add("Position/Authority");
        indexes.add("Id");
        indexes.add("Created At");

        List<MemberDto> memberDtos =  memList.stream().map(
                mem -> MemberDto.toDto(mem, defaultImageAddress)
        ).collect(Collectors.toList());

        List<Long > ids = memberDtos.stream().map(
                mem -> mem.getId()
        ).collect(Collectors.toList());
        Page<MemberDto> memberList = new CustomPageImpl<>(memberDtos, indexes, ids ,totalSize);//new ArrayList<>());

        return memberList;
    }

}

