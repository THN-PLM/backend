package server.thn.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import server.thn.Member.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RoleDto {
    Long id;
    private String name;

    public static RoleDto toDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getRoleType().name()
        );
    }

    public static List<RoleDto> toDtoList(List<Role> roleList) {

        List<RoleDto> roleDtos =
                roleList.stream().map(
                        role->
                                RoleDto.toDto(
                                        role
                                )
                ).collect(Collectors.toList());

        return roleDtos;
    }

}

