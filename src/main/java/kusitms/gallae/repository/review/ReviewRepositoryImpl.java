package kusitms.gallae.repository.review;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.QReview;
import kusitms.gallae.domain.Review;
import kusitms.gallae.dto.program.ProgramManagerReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static kusitms.gallae.domain.QProgram.program;
import static kusitms.gallae.domain.QReview.review;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    public ReviewRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Review> findReviewDynamicCategory(String category, Pageable pageable) {
        List<Review> reviews = this.jpaQueryFactory
                .selectFrom(review)
                .where(createReviewCondition(category))
                .orderBy(new OrderSpecifier<>(Order.DESC,review.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalSize = this.jpaQueryFactory
                .select(Wildcard.count)
                .from(review)
                .where(createReviewCondition(category))
                .fetchOne();
        return new PageImpl<>(reviews, pageable, Objects.requireNonNull(totalSize));
    }

    private BooleanBuilder createReviewCondition(String category) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(category != null) {
            booleanBuilder.and(review.category.eq(category));
        }

        return booleanBuilder;
    }


}
