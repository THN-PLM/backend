package server.thn.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Member.entity.DepartmentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSimpleDto {
    private Long id;
    private String email;
    private String username;
    private DepartmentType department;
    private String contact;
    private String profileImage;
}

