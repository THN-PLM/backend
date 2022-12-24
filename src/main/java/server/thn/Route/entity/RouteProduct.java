package server.thn.Route.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.thn.Common.entity.EntityDate;
import server.thn.Member.entity.Member;
import server.thn.Project.entity.Project;
import server.thn.Route.dto.routeProduct.RouteProductUpdateRequest;
import server.thn.Route.exception.RouteProductNotFoundException;
import server.thn.Route.repository.RouteProductRepository;

import javax.persistence.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@Table(name="route_product")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteProduct extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Long id;

    /**
     * 라우트에서의 순서를 나타냄
     */
    @Column(nullable = false)
    private Integer sequence;

    /**
     * 라우트 오더링에서 정의된 최초의 SEQ 를 나타냄
     */
    @Column(nullable = false)
    private Integer origin_seq;

    @Column(nullable = false)
    private String route_name;

    /**
     * request, approve, review, design, complete 중 하나
     */
    @ManyToOne
    @JoinColumn(name ="route_type")
    private RouteType type;

    /**
     * comment 남기기
     */
    @Column(nullable = false)
    @Lob
    private String comments;

    /**
     * false : 하기 전 , true : 실행 후
     */
    @Column(nullable = false)
    private boolean passed;

    /**
     * 거절당하고 새로 만들어진 라우트 제작물이라면
     * rejected = true (Rejected 투두)
     */
    @Column(nullable = false)
    private boolean rejected;

    /**
     * 06-01 : todo 에 rejected 띄우기 위한 용
     */
    @Column(nullable = false)
    private boolean preRejected;

    /**
     * 화면에 띄울 변수
     */
    @Column(nullable = false)
    private boolean route_show;

    @Column
    private boolean disabled;

    @Column
    private Integer refusal;

    @OneToMany(
            mappedBy = "routeProduct",
            cascade = CascadeType.ALL,//이거
            orphanRemoval = true, //없애면 안돼 동윤아...
            fetch = FetchType.LAZY
    )
    private List<RouteProductMember> members;
    /**
     * routeproduct 가 속하는 route 하나
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routeOrdering_id", nullable = false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private RouteOrdering routeOrdering;

    /**
     * null 가능, 플젝에서 라우트 생성 시 지정
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;
//
//    /**
//     * null 가능, 디자인에서 라우트 생성 시 지정
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "design_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Design design;
//
//    /**
//     * null 가능, 디자인에서 라우트 생성 시 지정
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "bom_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Bom bom;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cr_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private ChangeRequest changeRequest;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "co_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private ChangeOrder changeOrder;

    /**
     * 라우트 프로덕트 생성자 (reject 시 재 생산용)
     */
    public RouteProduct(
            Integer sequence,

            Integer origin_seq,

            String name,
            RouteType type,
            String comments,
            boolean passed,
            boolean rejected,
            boolean preRejected,
            boolean show,
            boolean disabled,
            Integer refusal,
            List<Member> member,
            RouteOrdering newRoute

    ) {
        this.sequence = sequence;

        this.origin_seq = origin_seq;


        this.route_name = name;

        this.type = type;
        this.comments = comments;
        this.passed = passed;
        this.rejected = rejected;
        this.preRejected = preRejected;
        this.route_show = show;
        this.disabled = disabled;
        this.refusal = refusal;
        this.members =
                member.stream().map(
                                r -> new RouteProductMember(
                                        this, r)
                        )
                        .collect(toList());
        this.routeOrdering = newRoute;
    }

    public RouteProduct(
            Integer sequence,

            Integer origin_seq,

            String name,
            RouteType type,
            String comments,
            boolean passed,
            boolean rejected,
            boolean preRejected,
            boolean show,
            boolean disabled,
            Integer refusal,
            List<Member> member,
            RouteOrdering newRoute,
            Project project
            // ,Design design
    ) {
        this.sequence = sequence;

        this.origin_seq = origin_seq;


        this.route_name = name;

        this.type = type;
        this.comments = comments;
        this.passed = passed;
        this.rejected = rejected;
        this.preRejected = preRejected;
        this.route_show = show;
        this.disabled = disabled;
        this.refusal = refusal;
        this.members =
                member.stream().map(
                                r -> new RouteProductMember(
                                        this, r)
                        )
                        .collect(toList());
        this.routeOrdering = newRoute;
        this.project = project;
        // this.design = design;
    }

    /**
     * 라우트 프로덕트 승인 시 업데이트
     * @param id
     * @param req
     * @param routeProductRepository
     * @return
     */
    public RouteProductUpdateRequest update(

            Long id,    /**기존 애들 용**/
            RouteProductUpdateRequest req,
            RouteProductRepository routeProductRepository

    ){

        RouteProduct routeProduct =
                routeProductRepository
                        .findById(id)
                        .orElseThrow(RouteProductNotFoundException::new);

        this.sequence = routeProduct.getSequence();
        this.route_name = routeProduct.getRoute_name();
        this.type = routeProduct.getType();
        this.comments = req.getComment();
        this.passed = true;
        this.rejected = false;
        this.refusal = -1;
        this.preRejected = false; //true 였다가

        this.routeOrdering = routeProduct.getRouteOrdering();

        this.route_show = true;
        this.disabled = false;
        return req;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public void setType(RouteType type) {
        this.type = type;
    }

    public void setComment(String comment) {
        this.comments = comment;
    }

    public void updateRefusal(Integer refusal) {
        this.refusal = refusal;
    }

    public void setShow(boolean show) {
        this.route_show = show;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setPreRejected(boolean preRejected) {
        this.preRejected = preRejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public void setProject(Project project) {
        this.project = project;
    }

//    public void setDesign(Design design) {
//        this.design = design;
//    }
//
//    public void setBom(Bom bom) {
//        this.bom = bom;
//    }
//
//    public void setChangeRequest(ChangeRequest changeRequest) {
//        this.changeRequest = changeRequest;
//    }
//
//    public void setChangeOrder(ChangeOrder changeOrder) {
//        this.changeOrder = changeOrder;
//    }

    // 권한 이양 시 멤버 추가
    public void addMember(Member member) {
        members.add(
                new RouteProductMember(
                        this, member)
        );
    }

}
