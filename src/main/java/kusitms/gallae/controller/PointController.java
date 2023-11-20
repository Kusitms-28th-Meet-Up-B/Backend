package kusitms.gallae.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.domain.Program;
import kusitms.gallae.dto.point.PointPageListRes;
import kusitms.gallae.dto.program.ProgramManagerReq;
import kusitms.gallae.dto.program.ProgramPageMangagerRes;
import kusitms.gallae.service.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    private PointService pointService;


    @Operation(summary = "사용자별 포인트 목록 가져오기", description = """
            로그인이 되어있어야합니다.\n
            type : 적립/사용  -> null이면 전체 \n
            period: 1주일/한달/3개월/6개월/1년 -> null이면 전체\n
            """)
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<PointPageListRes>> findProgramManagerProgress(
            Principal principal,

            @Parameter(description = "타입",example = "적립,사용")
            @RequestParam(value = "type", required = false)
            String type,

            @Parameter(description = "기간",example = "1주일,1달,3개월,6개월,1년")
            @RequestParam(value = "period", required = false)
            String period,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "0")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize
    ){
        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        return ResponseEntity.ok(new BaseResponse<>(this.pointService.getPointList(type,period,
                principal.getName(),pageRequest)));
    }
}
