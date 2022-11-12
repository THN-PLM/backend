package server.thn.Route.entity;

import lombok.*;
import server.thn.Common.entity.EntityDate;
import server.thn.Member.entity.Member;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(RouteProductMemberId.class)
public class RouteProductMember extends EntityDate {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeproduct_id")
    private RouteProduct routeProduct;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
