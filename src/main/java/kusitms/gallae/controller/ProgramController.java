package kusitms.gallae.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.dto.program.ProgramDetailRes;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.dto.program.ProgramMapRes;
import kusitms.gallae.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/programs")
public class ProgramController {

    private final ProgramService programService;

    @Operation(summary = "최신 프로그램 상위 4개", description = """
            최근에 등록된 프로그램 상위 4개를 반환 합니다.
            """)
    @GetMapping("/recent")
    public ResponseEntity<BaseResponse<List<ProgramMainRes>>> findRecentProgram(){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getRecentPrograms()));
    }

    @Operation(summary = "프로그램 유형별 프로그램들", description = """
            여행지원사업, 여행공모전, 여행대외활동 세가지 유형별로 프로그램들을 반환합니다.
            """)
    @GetMapping("/type")
    public ResponseEntity<BaseResponse<List<ProgramMainRes>>> findProgramsByProgramType(
            @Parameter(description = "프로그램 유형", example = "여행지원사업, 여행공모전, 여행대외활동")
            @RequestParam(value = "programType", required = true)
            String programType,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "1")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize
            ) {

        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getProgramsByProgramType(programType,pageRequest)));
    }

    @Operation(summary = "인기 많은 프로그램들", description = """
            찜수가 가장 높은 프로그램들을 상위 4개를 반환합니다.
            """)
    @GetMapping("/best")
    public ResponseEntity<BaseResponse<List<ProgramMainRes>>> findBestPrograms(){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getBestPrograms()));
    }

    @GetMapping("/program")
    public ResponseEntity<BaseResponse<ProgramDetailRes>> findProgramDetail(
            @Parameter(description = "프로그램 ID")
            @RequestParam(value = "id", required = false) Long id
    ){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getProgramDetail(id)));
    }
    @Operation(summary = "프로그램 지도에 필요한 값 보내주기", description = """
            위도,경도,사진,모집기간이 포함되어 있습니다 .
            일단 모든 데이터 넘겨주는 걸로 했습니다...
            지도는 첨이라..
            """)
    @GetMapping("/map")
    public ResponseEntity<BaseResponse<List<ProgramMapRes>>> findProgramDetail(
    ){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getProgramsMap()));
    }
}
