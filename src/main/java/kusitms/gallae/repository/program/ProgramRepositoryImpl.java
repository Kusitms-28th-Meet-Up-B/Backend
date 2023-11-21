package kusitms.gallae.repository.program;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kusitms.gallae.domain.Favorite;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.dto.program.ProgramManagerReq;
import kusitms.gallae.dto.program.ProgramSearchReq;
import kusitms.gallae.dto.program.ProgramSimilarReq;
import kusitms.gallae.global.DurationCalcurator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kusitms.gallae.domain.QFavorite.favorite;
import static kusitms.gallae.domain.QProgram.program;
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class ProgramRepositoryImpl implements ProgramRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    public Page<Program> getDynamicSearch(ProgramSearchReq programSearchReq){
        List<Program> programs = this.jpaQueryFactory
                .select(program)
                .from(program)
                .where(createSearchCondition(programSearchReq))
                .orderBy(createOrderSpecifier(programSearchReq))
                .offset(programSearchReq.getPageable().getOffset())
                .limit(programSearchReq.getPageable().getPageSize())
                .fetch();

        Long totalSize = this.jpaQueryFactory
                .select(Wildcard.count)
                .from(program)
                .where(createSearchCondition(programSearchReq))
                .fetchOne();

        return new PageImpl<>(programs, programSearchReq.getPageable(), Objects.requireNonNull(totalSize));
    }


    public Page<Program> getDynamicMananger(ProgramManagerReq programManagerReq){
        List<Program> programs = this.jpaQueryFactory
                .selectFrom(program)
                .where(createManagerProgramCondition(programManagerReq))
                .orderBy(new OrderSpecifier<>(Order.DESC,program.createdAt))
                .offset(programManagerReq.getPageable().getOffset())
                .limit(programManagerReq.getPageable().getPageSize())
                .fetch();

        Long totalSize = this.jpaQueryFactory
                .select(Wildcard.count)
                .from(program)
                .where(createManagerProgramCondition(programManagerReq))
                .fetchOne();
        return new PageImpl<>(programs, programManagerReq.getPageable(), Objects.requireNonNull(totalSize));
    }

    public List<Program> getDynamicSimilar(ProgramSimilarReq programSimilarReq){
        List<Program> programs = this.jpaQueryFactory
                .select(program)
                .from(program)
                .where(createSimiliarProgramCondition(programSimilarReq))
                .offset(0)
                .limit(4)
                .fetch();
        return programs;
    }

    private BooleanBuilder createManagerProgramCondition(ProgramManagerReq programManagerReq) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(programManagerReq.getProgramType() != null) {
            booleanBuilder.and(program.programType.contains(programManagerReq.getProgramType()));
        }

        if(programManagerReq.getStatus() != null) {
            booleanBuilder.and(program.status.eq(programManagerReq.getStatus()));
        }

        booleanBuilder.and(program.user.eq(programManagerReq.getUser()));

        return booleanBuilder;
    }

    private BooleanBuilder createSearchCondition(ProgramSearchReq programSearchReq) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(programSearchReq.getProgramName() != null) {
            booleanBuilder.and(program.programName.contains(programSearchReq.getProgramName()));
        }

        if(programSearchReq.getProgramType() != null) {
            booleanBuilder.and(program.programType.eq(programSearchReq.getProgramType()));
        }
        if(programSearchReq.getDetailType() != null) {
            booleanBuilder.and(program.detailType.eq(programSearchReq.getDetailType()));
        }
        if(programSearchReq.getLocation() != null) {
            booleanBuilder.and(program.location.contains(programSearchReq.getLocation()));
        }
        if(programSearchReq.getRecruitStartDate() != null) {
            booleanBuilder.and(program.recruitStartDate.goe(programSearchReq.getRecruitStartDate()));
        }
        if(programSearchReq.getRecruitEndDate() != null) {
            booleanBuilder.and(program.recruitEndDate.loe(programSearchReq.getRecruitEndDate()));
        }
        if(programSearchReq.getActiveStartDate() != null) {
            booleanBuilder.and(program.activeStartDate.goe(programSearchReq.getActiveStartDate()));
        }
        if(programSearchReq.getActiveEndDate() != null) {
            booleanBuilder.and(program.activeEndDate.loe(programSearchReq.getActiveEndDate()));
        }

        booleanBuilder.and(program.status.eq(Program.ProgramStatus.SAVE));
        return booleanBuilder;
    }

    private BooleanBuilder createSimiliarProgramCondition(ProgramSimilarReq programSimilarReq) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(programSimilarReq.getLocation() != null) {
            booleanBuilder.or(program.location.contains(programSimilarReq.getLocation()));
        }

        if(programSimilarReq.getProgramType() != null) {
            booleanBuilder.or(program.programType.eq(programSimilarReq.getProgramType()));
        }

        if(programSimilarReq.getId() != null) {
            booleanBuilder.and(program.id.ne(programSimilarReq.getId()));
        }

        booleanBuilder.and(program.status.eq(Program.ProgramStatus.SAVE));

        return booleanBuilder;
    }

    private OrderSpecifier<?> createOrderSpecifier(ProgramSearchReq programSearchReq) {

        if(programSearchReq.getOrderCriteria().equals("인기순")){
            return new OrderSpecifier<>(Order.DESC,program.programLike);
        }else if(programSearchReq.getOrderCriteria().equals("빠른마감순")){
           return new OrderSpecifier<>(Order.ASC, program.recruitEndDate);
        }else if(programSearchReq.getOrderCriteria().equals("늦은마감순")){
            return new OrderSpecifier<>(Order.DESC, program.recruitEndDate);
        }else{
            return new OrderSpecifier<>(Order.DESC,program.createdAt);
        }
    }

}
