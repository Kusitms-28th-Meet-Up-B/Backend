package kusitms.gallae.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.Review;
import kusitms.gallae.dto.program.ProgramManagerReq;
import kusitms.gallae.dto.review.*;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private S3Service s3Service;



    @Operation(summary = "리뷰 카테고리별 게시판 내용들 가져오기", description = """
            전체는 null로 보내주세요 
            """)
    @GetMapping("/category")
    public ResponseEntity<BaseResponse<ReviewPageRes>>  getReviewsByCategory(
            @Parameter(description = "여행 지원사업 / 여행 대외활동 / 여행 공모전")
            @RequestParam(value = "category", required = false)
            String category,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "0")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize){

        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        return ResponseEntity.ok(new BaseResponse<>(this.reviewService.getReviewsByCategory(category,pageRequest)));
    }

    @PostMapping("/saveReview")
    public ResponseEntity<BaseResponse> saveReview(
            Principal principal,

            @ModelAttribute
            ReviewModel reviewModel
    ) throws IOException {
        String fileName = null;
        String fileUrl = null;
        System.out.println(principal.getName());
        if(reviewModel.getFile() != null && !reviewModel.getFile().isEmpty()) {
            fileName = reviewModel.getFile().getName();
            fileUrl = s3Service.upload(reviewModel.getFile());
        }
        ReviewPostReq reviewPostReq = new ReviewPostReq();
        reviewPostReq.setTitle(reviewModel.getTitle());
        reviewPostReq.setWriter(reviewPostReq.getWriter());
        reviewPostReq.setCategory(reviewPostReq.getCategory());
        reviewPostReq.setFileUrl(fileUrl);
        reviewPostReq.setFileName(fileName);
        reviewPostReq.setBody(reviewModel.getBody());
        reviewPostReq.setHashTags(reviewModel.getHashTags());
        reviewService.postReivew(reviewPostReq,principal.getName());
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));

    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<BaseResponse<ReviewDetailRes>> getReviewDetail(@PathVariable Long id) {
        ReviewDetailRes reviewDetail = reviewService.getReviewById(id);
        {
            return ResponseEntity.ok(new BaseResponse<>(reviewDetail));

        }
    }

}