package kusitms.gallae.repository.point;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kusitms.gallae.domain.Point;
import kusitms.gallae.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static kusitms.gallae.domain.QPoint.point;
import static kusitms.gallae.domain.QProgram.program;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class PointRepositoryImpl implements PointRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Point> getDynamicPoint(User user, String type, String period, Pageable pageable ){
        List<Point> points = this.jpaQueryFactory
                .select(point)
                .from(point)
                .where(createPointCondition(user, type, period))
                .orderBy(new OrderSpecifier<>(Order.DESC,point.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalSize = this.jpaQueryFactory
                .select(Wildcard.count)
                .from(point)
                .where(createPointCondition(user, type,period))
                .fetchOne();

        return new PageImpl<>(points, pageable , Objects.requireNonNull(totalSize));
    }

    private BooleanBuilder createPointCondition(User user, String type, String period) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(point.user.eq(user));
        if(type != null) {
            booleanBuilder.and(point.pointCategory.contains(type));
        }

        if(period != null) {
            LocalDateTime now = LocalDateTime.now();
            if(period.contains("1주일")) {
                booleanBuilder.and(point.createdAt.goe(now.minusDays(7)));
            }else if(period.contains("1달")){
                booleanBuilder.and(point.createdAt.goe(now.minusMonths(1)));
            }else if(period.contains("3개월")) {
                booleanBuilder.and(point.createdAt.goe(now.minusMonths(3)));
            }else if(period.contains("6개월")) {
                booleanBuilder.and(point.createdAt.goe(now.minusMonths(6)));
            }else{
                booleanBuilder.and(point.createdAt.goe(now.minusYears(1)));
            }
        }
        return booleanBuilder;
    }
}
