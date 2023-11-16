package kusitms.gallae.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.dto.model.PostModel;
import kusitms.gallae.dto.program.*;
import kusitms.gallae.dto.tourapi.TourApiDto;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.service.program.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/programs")
public class ProgramController {

    private final ProgramService programService;


    private final S3Service s3Service;


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
    public ResponseEntity<BaseResponse<ProgramPageMainRes>> findProgramsByProgramType(
            @Parameter(description = "프로그램 유형", example = "여행지원사업, 여행공모전, 여행대외활동")
            @RequestParam(value = "programType", required = true)
            String programType,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "0")
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

    @Operation(summary = "프로그램 이름으로 검색", description = """
            프로그램 이름이 포함된 프로그램들을 반환합니다
            
            여기서 TotalSize는 페이지의 총 갯수를 나타내며 
            pageNumber는 0 부터 시작 
            TotalSize는 1부터 시작입니다.
            즉 TotalSize가 4를 가리키면
            pageNumber는 0~3 까지 4개 있는 겁니다.
            """)
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<ProgramPageMainRes>> findProgramsByProgramName(
            @Parameter(description = "프로그램 이름", example = "test")
            @RequestParam(value = "programName", required = true)
            String programName,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "0")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize
    ) {

        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getProgramsByProgramName(programName,pageRequest)));
    }

    @Operation(summary = "필터로 프로그램 검색", description = """
            필터 조건에 맞게 프로그램 검색을 합니다.
            필수 입력값이 orderCriteria(정렬기준) 이며 나머지는 null로 보내주셔도 됩니다.
            ** 주의  ** 
            피그마에 전체로 되어있는데 전체를 선택시 null로 보내주시면 됩니다.
            
            여기서 TotalSize는 페이지의 총 갯수를 나타내며 
            pageNumber는 0 부터 시작 
            TotalSize는 1부터 시작입니다.
            즉 TotalSize가 4를 가리키면
            pageNumber는 0~3 까지 4개 있는 겁니다.
            """)
    @GetMapping("/filters")
    public ResponseEntity<BaseResponse<ProgramPageMainRes>> findProgramsByFilter(

            @Parameter(description = "프로그램 이름", example = "이름")
            @RequestParam(value = "programName")
            String programName,

            @Parameter(description = "정렬 기준", example = "최신순, 인기순 , 빠른마감순,늦은마감순")
            @RequestParam(value = "orderCriteria", required = true)
            String orderCriteria,

            @Parameter(description = "위치", example = "충북")
            @RequestParam(value = "location", required = false)
            String location,

            @Parameter(description = "여행 타입", example = "여행지원사업,여행공모전,여행대외활동")
            @RequestParam(value = "programType", required = false)
            String programType,

            @Parameter(description = "여행 세부사항 타입", example = "지자체 한달살이, 여행사진 공모전 등")
            @RequestParam(value = "detailType", required = false)
            String detailType,

            @Parameter(description = "모집 시작 날짜 ", example = "2023-11-01")
            @RequestParam(value = "recruitStartDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate recruitStartDate,

            @Parameter(description = "모집 마감 날짜", example = "2023-11-01")
            @RequestParam(value = "recruitEndDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate recruitEndDate,

            @Parameter(description = "활동 시작 날짜", example = "2023-11-01")
            @RequestParam(value = "activeStartDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate activeStartDate,

            @Parameter(description = "활동 마감 날짜", example = "2023-11-01")
            @RequestParam(value = "activeEndDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate activeEndDate,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "0")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize
    ) {
        ProgramSearchReq programSearchReq = new ProgramSearchReq();
        programSearchReq.setProgramName(programName);
        programSearchReq.setOrderCriteria(orderCriteria);
        programSearchReq.setLocation(location);
        programSearchReq.setProgramType(programType);
        programSearchReq.setDetailType(detailType);
        programSearchReq.setRecruitStartDate(recruitStartDate);
        programSearchReq.setRecruitEndDate(recruitEndDate);
        programSearchReq.setActiveStartDate(activeStartDate);
        programSearchReq.setActiveEndDate(activeEndDate);
        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        programSearchReq.setPageable(pageRequest);
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getProgramsByDynamicQuery(programSearchReq)));
    }


    @Operation(summary = "인기 많은 프로그램들", description = """
            찜수가 가장 높은 프로그램들을 상위 4개를 반환합니다.
            """)
    @GetMapping("/best")
    public ResponseEntity<BaseResponse<List<ProgramMainRes>>> findBestPrograms(){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getBestPrograms()));
    }

    @Operation(summary = "프로그램 세부내용 가져오기")
    @GetMapping("/program")
    public ResponseEntity<BaseResponse<ProgramDetailRes>> findProgramDetail(
            @Parameter(description = "프로그램 ID")
            @RequestParam(value = "id", required = true) Long id
    ){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getProgramDetail(id)));
    }

    @Operation(summary = "프로그램 지역에 맞게 여행 추천해주기")
    @GetMapping("/regionTour")
    public ResponseEntity<BaseResponse<List<TourApiDto>>> findTourbyProgramRegion(
            @Parameter(description = "프로그램 ID")
            @RequestParam(value = "id", required = true) Long id
    ){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getTourDatas(id)));
    }

    @Operation(summary = "유사한 프로그램 추천", description = """
            지역과 프로그램 타입이 같은 프로그램 4개를 반환합니다.
            해당 프로그램은 없을 수도 있습니다.
            만약에 다른 방법이 제시 되면 수정하겠습니다.
            """)
    @GetMapping("/similarRecommend")
    public ResponseEntity<BaseResponse<List<ProgramMainRes>>> findTourbySimilarPrograms(
            @Parameter(description = "프로그램 ID")
            @RequestParam(value = "id", required = true) Long id
    ){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getSimilarPrograms(id)));
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
