package server.thn.Route.entity;

import lombok.*;
import server.thn.Member.entity.Member;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RouteProductMemberId implements Serializable {
    private RouteProduct routeProduct;
    private Member member;
}

