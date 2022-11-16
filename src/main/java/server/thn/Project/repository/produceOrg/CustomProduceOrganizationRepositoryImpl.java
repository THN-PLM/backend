package server.thn.Project.repository.produceOrg;


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
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.dto.ReadResponse;
import server.thn.Project.entity.ProjectType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static server.thn.Project.entity.QBuyerOrganization.buyerOrganization;
import static server.thn.Project.entity.QProduceOrganization.produceOrganization;

@Transactional(readOnly = true)
public class CustomProduceOrganizationRepositoryImpl extends QuerydslRepositorySupport implements CustomProduceOrganizationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomProduceOrganizationRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(ProjectType.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ReadResponse> findAllByCondition( ReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List< ReadResponse> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                 ReadResponse.class,
                                produceOrganization.id,
                                produceOrganization.code1,
                                produceOrganization.code2,
                                produceOrganization.department.produceOrganizationClassification1.name,
                                produceOrganization.department.produceOrganizationClassification2.name
                        ))
                        .from(produceOrganization)
                        .where(predicate)
                        .orderBy(produceOrganization.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory.select(
                        produceOrganization.count()
                ).from(produceOrganization).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate( ReadCondition cond) {
        return new BooleanBuilder()
                .and(orConditionsByEqNames(cond.getName()))
                .and(orConditionsByNotDeleted( ))
                ;
    }

    private Predicate orConditionsByNotDeleted( ) {
        return produceOrganization.notDeleted.isTrue();
    }


    private Predicate orConditionsByEqNames(String word) {
        List<String> words = new ArrayList<>();
        words.add(word);
        return orConditions(words, produceOrganization.code1::containsIgnoreCase);
        //검색어로 넘어온 단어가 이름에 contain (포함) 되어있는지
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) {
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }



}
