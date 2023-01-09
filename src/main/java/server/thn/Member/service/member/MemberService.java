package server.thn.Member.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Common.page.CustomPageImpl;
import server.thn.Member.dto.MemberDto;
import server.thn.Member.dto.MemberListDto;
import server.thn.Member.dto.MemberReadCondition;
import server.thn.Member.entity.Member;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    public MemberDto read(Long id) {
        return MemberDto.toDto(
                memberRepository.findById(id).orElseThrow(MemberNotFoundException::new),
                defaultImageAddress
        );
    }

    @Transactional
    public void delete(Long id) {
        if(notExistsMember(id)) throw new MemberNotFoundException();
        memberRepository.deleteById(id);
    }

    private boolean notExistsMember(Long id) {
        return !memberRepository.existsById(id);
    }

    public MemberListDto readAll(MemberReadCondition cond) {
        return MemberListDto.toDto(
                memberRepository.findAllByCondition(cond)
        );
    }

    // paging member search 처리

    public List<Member> searchMemberWithKeywords(@Nullable String search) {

        List<Member> totalList = new ArrayList<>();

        if(search!=null&&search.length()>0&&!search.contains("\n")){
            totalList  = memberRepository.findByUsernameContainingIgnoreCase(
                    search
            );

        }else {
            totalList = memberRepository.findAll();
        }

        return totalList;
    }

    // dto list 로 반환
    public List<MemberDto> memberDtoConvert(@Nullable String search) {
        if(search!=null&&search.length()>0&&!search.contains("\n")){
            return searchMemberWithKeywords( search ).stream().map(
                    mem -> MemberDto.toDto(mem, defaultImageAddress)
            ).collect(Collectors.toList());
        }
        return memberRepository.findAll().stream().map(
                mem -> MemberDto.toDto(mem, defaultImageAddress)
        ).collect(Collectors.toList());
    }

    public Page<MemberDto> returnPagingProject(
            String name,
            Pageable pageRequest

    ){
        Page<Member> memList = memberRepository.findByMembers(searchMemberWithKeywords(name), pageRequest);

        return memList.map(
                d -> MemberDto.toDto(d, defaultImageAddress)
        );
    }

    public Page<MemberDto> returnCustomPagingProject(
            String name,
            Pageable pageRequest
    ){
        int totalSize = 0;

        // 인덱스 커스텀
        List<String> indexes = new ArrayList<>();
        indexes.add("Name");
        indexes.add("Department");
        indexes.add("Position/Authority");

        List<Long > ids = memberDtoConvert(name).stream().map(
                mem -> mem.getId()
        ).collect(Collectors.toList());

        if(name!=null&&name.length()>0&&!name.contains("\n")){
            totalSize+=memberRepository.findByUsernameContainingIgnoreCase(
                    name
            ).size();
        }
        else{
            totalSize+=memberRepository.findAll().size();
        }

        List<MemberDto> content = memberDtoConvert(name);

        return new CustomPageImpl<>(content,pageRequest, totalSize, indexes, ids ,totalSize, pageRequest.getPageSize() ,pageRequest.getPageNumber());
    }



}
