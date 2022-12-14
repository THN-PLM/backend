package server.thn.Project.repository.buyer;

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
import server.thn.Project.repository.produceOrg.CustomProduceOrganizationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static server.thn.Project.entity.QBuyerOrganization.buyerOrganization;
import static server.thn.Project.entity.QProduceOrganization.produceOrganization;

@Transactional(readOnly = true)
public class CustomBuyerOrganizationRepositoryImpl extends QuerydslRepositorySupport implements CustomBuyerOrganizationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomBuyerOrganizationRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(ProjectType.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ReadResponse> findAllByCondition(ReadCondition cond) {
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
                                buyerOrganization.id,
                                buyerOrganization.code1,
                                buyerOrganization.code2,
                                buyerOrganization.department.classification1.name,
                                buyerOrganization.department.classification2.name
                        ))
                        .from(buyerOrganization)
                        .where(predicate)
                        .orderBy(buyerOrganization.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory.select(
                        buyerOrganization.count()
                ).from(buyerOrganization).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate( ReadCondition cond) {
        return new BooleanBuilder()
                .and(orConditionsByEqNames(cond.getName()))
                .and(orConditionsByNotDeleted( ))
                ;
    }

    private Predicate orConditionsByNotDeleted( ) {
        return buyerOrganization.notDeleted.isTrue();
    }


    private Predicate orConditionsByEqNames(String word) {
        List<String> words = new ArrayList<>();
        words.add(word);
        return orConditions(words, buyerOrganization.code1::containsIgnoreCase);
        //???????????? ????????? ????????? ????????? contain (??????) ???????????????
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) {
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }



}
