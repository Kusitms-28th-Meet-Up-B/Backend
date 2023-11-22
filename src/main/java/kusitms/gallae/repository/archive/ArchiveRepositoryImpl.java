package kusitms.gallae.repository.archive;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kusitms.gallae.domain.Archive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static kusitms.gallae.domain.QArchive.archive;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class ArchiveRepositoryImpl implements ArchiveRespositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Archive> findArchiveDynamicCategory(String category, String title, Pageable pageable) {
        BooleanBuilder condition = createArchiveCondition(category, title);

        List<Archive> archives = jpaQueryFactory
                .selectFrom(archive)
                .where(condition)
                .orderBy(new OrderSpecifier<>(Order.DESC, archive.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalSize = jpaQueryFactory
                .select(Wildcard.count)
                .from(archive)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(archives, pageable, Objects.requireNonNull(totalSize));
    }

    private BooleanBuilder createArchiveCondition(String category, String title) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (category != null) {
            booleanBuilder.and(archive.category.eq(category));
        }
        if (title != null && !title.trim().isEmpty()) {
            booleanBuilder.and(archive.title.contains(title));
        }

        return booleanBuilder;
    }

}
