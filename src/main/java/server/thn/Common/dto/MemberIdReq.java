package server.thn.Common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MemberIdReq {
    private Long memberId;
}
