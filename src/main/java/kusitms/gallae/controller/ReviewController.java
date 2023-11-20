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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
            @Parameter(description = "여행지원사업 / 여행대외활동 / 여행공모전")
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
    public ResponseEntity<BaseResponse<Long>> saveReview(
            Principal principal,

            @ModelAttribute
            ReviewModel reviewModel
    ) throws IOException {
        String fileUrl = null;
        String originalFilename = null; // 원본 파일 이름을 저장할 변수

        if (reviewModel.getFile() != null && !reviewModel.getFile().isEmpty()) {
            originalFilename = reviewModel.getFile().getOriginalFilename(); // 원본 파일 이름 가져오기
            fileUrl = s3Service.upload(reviewModel.getFile());
        }

        ReviewPostReq reviewPostReq = new ReviewPostReq();
        reviewPostReq.setTitle(reviewModel.getTitle());
        reviewPostReq.setWriter(reviewPostReq.getWriter());
        reviewPostReq.setCategory(reviewPostReq.getCategory());
        reviewPostReq.setFileUrl(fileUrl);
        reviewPostReq.setFileName(originalFilename);
        reviewPostReq.setBody(reviewModel.getBody());
        reviewPostReq.setHashTags(reviewModel.getHashTags());

        return ResponseEntity.ok(new BaseResponse<>(reviewService.postReivew(reviewPostReq,principal.getName())));

    }

    @GetMapping("/detail")
    public ResponseEntity<BaseResponse<ReviewDetailRes>> getReviewDetail(
            Principal principal,

            @Parameter(description = "지원후기아이디")
            @RequestParam(value = "reviewId", required = false)
            Long reviewId
    ) {
        String username = null;
        if(principal != null) {
            username = principal.getName();
        }
        ReviewDetailRes reviewDetail = reviewService.getReviewById(reviewId, username);
        return ResponseEntity.ok(new BaseResponse<>(reviewDetail));
    }


    @Operation(summary = "지원후기 좋아요순으로 게시판 내용들 가져오기")
    @GetMapping("/sorted/likes")
    public ResponseEntity<BaseResponse<List<ReviewDtoRes>>> getAllReviewsSortedByLikes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes"));
        Page<Review> reviewPage = reviewService.getAllReviewsSortedByLikes(pageRequest);
        List<ReviewDtoRes> reviewDtos = reviewPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        BaseResponse<List<ReviewDtoRes>> response = new BaseResponse<>(
                true,
                "Success",
                reviewPage.getNumber(),
                reviewDtos
        );

        return ResponseEntity.ok(response);
    }

    // Review 엔티티를 ReviewDtoRes로 변환하는 메소드
    private ReviewDtoRes convertToDto(Review review) {
        ReviewDtoRes dto = new ReviewDtoRes();
        dto.setId(review.getId());
        dto.setCategory(review.getCategory());
        dto.setTitle(review.getTitle());
        dto.setWriter(review.getWriter());
        dto.setCreatedDate(review.getCreatedAt());
        return dto;
    }

}