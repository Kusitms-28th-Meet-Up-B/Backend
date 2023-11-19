package kusitms.gallae.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.dto.favorite.FavoriteSearchReq;
import kusitms.gallae.dto.program.ProgramDetailRes;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.service.favorite.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Operation(summary = "프로그램 좋아요 누르기", description = """
            유저가 프로그램 좋아요를 누른다.
            프로그램 좋아요 갯수가 오른다.
            유저 마이페이지에 프로그램 추가된다.
            이미 좋아요 눌렀던 프로그램이면 "코드 410 ,이미 존재합니다" 메시지 나옴
            """)
    @PostMapping("/program")
    public ResponseEntity<BaseResponse> postFavoriteProgram(
            Principal principal,
            @Parameter(description = "프로그램 ID")
            @RequestParam(value = "programId", required = true) Long programId
    ){
        favoriteService.postFavorite(principal.getName(),programId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
    }


    @Operation(summary = "유저 별 좋아요 누른 프로그램 출력", description = """
            유저별로 좋아요 누른 프로그램 출력한다. 
            로그인이 필요합니다.
            programStatus 는 null로 보내면 자동으로 모집중 상태를 뽑아옵니다.
            null이 아니면 모집마감 넣으시면 됩니다.
            """)
    @GetMapping("/mypage")
    public ResponseEntity<BaseResponse<List<ProgramMainRes>>> findFavoritePrograms(
            Principal principal,

            @Parameter(description = "지역")
            @RequestParam(value = "region", required = false)
            String region,

            @Parameter(description = "프로그램 타입")
            @RequestParam(value = "programType", required = false)
            String programType,

            @Parameter(description = "진행상태")
            @RequestParam(value = "programStatus", required = false)
            String status
    ){
        FavoriteSearchReq favoriteSearchReq = new FavoriteSearchReq();
        favoriteSearchReq.setLocation(region);
        favoriteSearchReq.setProgramType(programType);
        favoriteSearchReq.setStatus(status);

        return ResponseEntity.ok(new BaseResponse<>(favoriteService.getFavoriteByUser(principal.getName(),favoriteSearchReq)));
    }
}
