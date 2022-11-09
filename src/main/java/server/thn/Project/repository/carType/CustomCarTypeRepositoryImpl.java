package server.thn.Project.repository.carType;

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
import server.thn.Project.dto.carType.CarTypeReadCondition;
import server.thn.Project.dto.carType.CarTypeReadResponse;
import server.thn.Project.entity.CarType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static server.thn.Project.entity.QCarType.carType;

@Transactional(readOnly = true)
public class CustomCarTypeRepositoryImpl extends QuerydslRepositorySupport implements CustomCarTypeRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomCarTypeRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CarType.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<CarTypeReadResponse> findAllByCondition(CarTypeReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<CarTypeReadResponse> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                CarTypeReadResponse.class,
                                carType.id,
                                carType.name
                        ))
                        .from(carType)
                        .where(predicate)
                        .orderBy(carType.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory.select(
                        carType.count()
                ).from(carType).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(CarTypeReadCondition cond) {
        return new BooleanBuilder()
                .and(orConditionsByEqNames(cond.getName()));
    }

    private Predicate orConditionsByEqNames(String word) {
        List<String> words = new ArrayList<>();
        words.add(word);
        return orConditions(words, carType.name::containsIgnoreCase);
    }

    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

}
