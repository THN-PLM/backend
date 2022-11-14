package server.thn.Member.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;
import server.thn.Member.dto.MemberReadCondition;
import server.thn.Member.dto.MemberSimpleDto;
import server.thn.Member.entity.Member;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static server.thn.Member.entity.QMember.member;
import static server.thn.Member.entity.QProfileImage.profileImage;

@Transactional(readOnly = true)
public class CustomMemberRepositoryImpl extends QuerydslRepositorySupport implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomMemberRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Member.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<MemberSimpleDto> findAllByCondition(MemberReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<MemberSimpleDto> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                MemberSimpleDto.class,
                                member.id,
                                member.email,
                                member.username,
                                member.department.departmentType,
                                member.contact,
                                profileImage.imageaddress
                        ))
                        .from(member)

                        .join(profileImage).on(member.id.eq(profileImage.member.id))

                        .where(predicate)
                        .orderBy(member.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        member.count()
                ).from(member).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(MemberReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}

