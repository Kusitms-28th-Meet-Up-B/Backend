package kusitms.gallae.repository.program;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kusitms.gallae.domain.Program;
import kusitms.gallae.dto.program.ProgramSearchReq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kusitms.gallae.domain.QProgram.program;
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class ProgramRepositoryImpl implements ProgramRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    public Page<Program> getDynamicSearch(ProgramSearchReq programSearchReq){
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(programSearchReq);
        List<Program> programs = this.jpaQueryFactory
                .selectFrom(program)
                .where(createSearchCondition(programSearchReq))
                .orderBy(orderSpecifiers)
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

    private BooleanBuilder createSearchCondition(ProgramSearchReq programSearchReq) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

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
        return booleanBuilder;
    }

    private OrderSpecifier[] createOrderSpecifier(ProgramSearchReq programSearchReq) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if(programSearchReq.getOrderCriteria() == "인기순"){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC,program.programLike));
        }else if(programSearchReq.getOrderCriteria() == "빠른마감순"){
            orderSpecifiers.add(new OrderSpecifier(Order.ASC, program.recruitEndDate));
        }else if(programSearchReq.getOrderCriteria() == "늦은마감순"){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, program.recruitEndDate));
        }else{
            orderSpecifiers.add(new OrderSpecifier(Order.DESC,program.createdAt));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

}
