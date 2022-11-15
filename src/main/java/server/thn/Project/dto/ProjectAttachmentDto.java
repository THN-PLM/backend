package server.thn.Project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Common.dto.AttachmentTagDto;
import server.thn.File.repository.AttachmentTagRepository;
import server.thn.Project.entity.ProjectAttachment;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class ProjectAttachmentDto implements Comparable<ProjectAttachmentDto>  {
    private Long id;
    private String originName;
    private String uniqueName;
    private String attach_comment;
    private boolean deleted;
    private AttachmentTagDto tag;
    private String attachmentaddress;
    private String date;
    private String upload;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    @Transactional
    public static ProjectAttachmentDto toDto(
            ProjectAttachment attachment,
            AttachmentTagRepository attachmentTagRepository
    ) {
        return new ProjectAttachmentDto(
                attachment.getId(),
                attachment.getOriginName(),
                attachment.getUniqueName(),
                attachment.getAttach_comment(),
                attachment.isDeleted(),
                //attachment.getTag(),
                AttachmentTagDto.toDto(
                        attachmentTagRepository.findByName(
                                attachment.getTag()
                        )
                ),
                "src/main/prodmedia/image/"
                        +
                        attachment.
                                getCreatedAt().
                                toString().
                                split("_")[0].
                                substring(0,10)
                        + "/"  +
                        attachment.getUniqueName(),

                attachment.getModifiedAt().toString().split("_")[0].substring(0, 10),

                attachment.getProject().getMember().getUsername(),

                attachment.getModifiedAt()

        );
    }


    public static List<ProjectAttachmentDto> toDtoList(
            List<ProjectAttachment> Attachments,
            AttachmentTagRepository attachmentTagRepository
    ) {

        List<ProjectAttachmentDto> attachmentDtoList =
                Attachments.stream().map(
                        i -> new ProjectAttachmentDto(
                                i.getId(),
                                i.getOriginName(),
                                i.getUniqueName(),
                                i.getAttach_comment(),
                                i.isDeleted(),
                                //i.getTag(),
                                AttachmentTagDto.toDto(
                                        attachmentTagRepository.findByName(i.getTag())
                                ),
                                "src/main/prodmedia/image/"
                                        +
                                        i.
                                                getCreatedAt().
                                                toString().
                                                split("_")[0].
                                                substring(0,10)
                                        + "\\"  +
                                        i.getUniqueName(),

                                i.getModifiedAt().toString().split("_")[0].substring(0, 10),
                                i.getProject().getMember().getUsername(),
                                i.getModifiedAt()
                        )
                ).collect(
                        toList()
                );

        return attachmentDtoList;

    }


    @Override
    public int compareTo(ProjectAttachmentDto attachment) {
        return (int) (this.id - attachment.getId());
    }

}