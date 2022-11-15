package server.thn.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadResponse{
    private Long id;
    private String code1;
    private String code2;
    private String name1;
    private String name2;
}
