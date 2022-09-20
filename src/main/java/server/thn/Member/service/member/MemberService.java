package server.thn.Member.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Member.dto.MemberDto;
import server.thn.Member.dto.MemberListDto;
import server.thn.Member.dto.MemberReadCondition;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Member.repository.MemberRepository;

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

}
