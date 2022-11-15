package server.thn.Common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.File.entity.AttachmentTag;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentTagDto {
    private Long id;
    private String name;

    public static AttachmentTagDto toDto(
            AttachmentTag attachmentTag) {

        return new AttachmentTagDto(
                attachmentTag.getId(),
                attachmentTag.getName()
        );
    }


    public static AttachmentTagDto toDto() {
        return new  AttachmentTagDto(
        );
    }

}

