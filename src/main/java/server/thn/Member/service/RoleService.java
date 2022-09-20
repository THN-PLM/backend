package server.thn.Member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Member.dto.RoleDto;
import server.thn.Member.repository.RoleRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleDto> readAll() {
        return RoleDto.toDtoList(
                roleRepository.findAll()
        );
    }

}
