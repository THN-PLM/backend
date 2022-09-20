package server.thn.Member.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Member.entity.Member;
import server.thn.Member.entity.MemberRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String username;
    private String department;
    private String contact;
    private String profileImage;
    private String position;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private boolean admin;

    public static MemberDto toDto(Member member,
                                  String defaultImageAddress) {

        List<String> rolesToStr = new ArrayList<>();
        boolean adminExist = false;

        for(MemberRole role : member.getRoles()){
            rolesToStr.add(role.getRole().getRoleType().name());
            if(role.getRole().getRoleType().name().equals("ROLE_ADMIN")){
                adminExist = true;
            }
        }

        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getUsername(),
                //member.getDepartment().get,

                (
                        member.getDepartment().getClassification1().getName() + "/"
                                + member.getDepartment().getClassification2().getName() + "/"

                                + (member.getDepartment().getClassification3().getId().equals(99999L) ?
                                " " :
                                member.getDepartment().getClassification3().getName())

                                + (member.getDepartment().getClassification4().getId().equals(99999L) ?
                                " " :
                                "/" + member.getDepartment().getClassification4().getName()
                        )

                ),



                member.getContact(),
                member.getProfileImage()==null?
                        defaultImageAddress :
                        member.getProfileImage().getImageaddress(),

                rolesToStr.toString().replace("[", "").replace("]", "").replace(" ", ""),

                member.getCreatedAt(),
                adminExist
        );
    }

    public static List <MemberDto> toDtoList(
            List<Member> members,
            String defaultImageAddress
    ) {

        List<MemberDto> memberDtos = new ArrayList<>();

        for(Member member : members) {

            List<String> rolesToStr = new ArrayList<>();
            boolean adminExist = false;

            for(MemberRole role : member.getRoles()){
                rolesToStr.add(role.getRole().getRoleType().name());
                if(role.getRole().getRoleType().name().equals("ROLE_ADMIN"));{
                    adminExist = true;
                }
            }

            if (member.getProfileImage()!=null) {



                MemberDto memberDto = new MemberDto(
                        member.getId(),
                        member.getEmail(),
                        member.getUsername(),

                        (
                                member.getDepartment().getClassification1().getName() + "/"
                                        + member.getDepartment().getClassification2().getName() + "/"

                                        + (member.getDepartment().getClassification3().getId().equals(99999L) ?
                                        " " :
                                        member.getDepartment().getClassification3().getName())


                                        + (member.getDepartment().getClassification4().getId().equals(99999L) ?
                                        " " :
                                        "/" + member.getDepartment().getClassification4().getName()
                                )

                        ),


                        member.getContact(),
                        member.getProfileImage().getImageaddress(),

                        rolesToStr.toString().replace("[", "").replace("]", "").replace(" ", ""),

                        member.getCreatedAt(),

                        adminExist

                );
                memberDtos.add(memberDto);
            }else{
                MemberDto memberDto = new MemberDto(
                        member.getId(),
                        member.getEmail(),
                        member.getUsername(),

                        (
                                member.getDepartment().getClassification1().getName() + "/"
                                        + member.getDepartment().getClassification2().getName() + "/"

                                        + (member.getDepartment().getClassification3().getId().equals(99999L) ?
                                        " " :
                                        "/" + member.getDepartment().getClassification3().getName())

                                        + (member.getDepartment().getClassification4().getId().equals(99999L) ?
                                        " " :
                                        "/" + member.getDepartment().getClassification4().getName()
                                )

                        ),


                        member.getContact(),
                        defaultImageAddress,

                        rolesToStr.toString().replace("[", "").replace("]", "").replace(" ", ""),

                        member.getCreatedAt(),

                        adminExist
                );
                memberDtos.add(memberDto);
            }
        }

        return memberDtos;
    }

}
