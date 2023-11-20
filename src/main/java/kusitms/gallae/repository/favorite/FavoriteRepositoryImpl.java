package kusitms.gallae.repository.favorite;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kusitms.gallae.domain.Program;
import kusitms.gallae.dto.favorite.FavoriteSearchReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kusitms.gallae.domain.QArchive.archive;
import static kusitms.gallae.domain.QFavorite.favorite;
import static kusitms.gallae.domain.QProgram.program;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Program> getFavoritePrograms(FavoriteSearchReq favoriteSearchReq) {
        List<Program> programs = this.jpaQueryFactory
                .select(program)
                .from(favorite)
                .leftJoin(favorite.program,program)
                .where(createFavoriteCondition(favoriteSearchReq))
                .orderBy(new OrderSpecifier<>(Order.DESC,program.createdAt))
                .fetch();

        return programs;
    }

    private BooleanBuilder createFavoriteCondition(FavoriteSearchReq favoriteSearchReq) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(favorite.user.eq(favoriteSearchReq.getUser()));

        if(favoriteSearchReq.getLocation() != null){
            booleanBuilder.and(program.location.contains(favoriteSearchReq.getLocation()));
        }

        if(favoriteSearchReq.getProgramType() != null){
            booleanBuilder.and(program.programType.contains(favoriteSearchReq.getProgramType()));
        }

        if(favoriteSearchReq.getStatus() != null){
            if(favoriteSearchReq.getStatus().contains("모집 중")) {
                booleanBuilder.and(program.status.eq(Program.ProgramStatus.SAVE));
            }else{
                booleanBuilder.and(program.status.eq(Program.ProgramStatus.FINISH));
            }
        }

        return booleanBuilder;
    }
}
