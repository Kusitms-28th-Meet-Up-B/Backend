package kusitms.gallae.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.Review;
import kusitms.gallae.dto.archive.ArchiveDetailRes;
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
        reviewPostReq.setWriter(reviewModel.getWriter());
        reviewPostReq.setCategory(reviewModel.getCategory());
        reviewPostReq.setFileUrl(fileUrl);
        reviewPostReq.setFileName(originalFilename);
        reviewPostReq.setBody(reviewModel.getBody());
        reviewPostReq.setHashTags(reviewModel.getHashTags());

        return ResponseEntity.ok(new BaseResponse<>(reviewService.postReivew(reviewPostReq,principal.getName())));

    }

    @Operation(summary = "리뷰 편집", description = """
            저장이랑 다르게 
            reivewId도 같이 보내야합니다.
            """)
    @PostMapping("/editReview")
    public ResponseEntity<BaseResponse<Long>> editReview(
            @ModelAttribute
            ReviewEditModel reviewEditModel
    ) throws IOException {
        String fileUrl = null;
        String originalFilename = null; // 원본 파일 이름을 저장할 변수

        if (reviewEditModel.getFile() != null && !reviewEditModel.getFile().isEmpty()) {
            originalFilename = reviewEditModel.getFile().getOriginalFilename(); // 원본 파일 이름 가져오기
            fileUrl = s3Service.upload(reviewEditModel.getFile());
        }

        ReviewEditReq reviewEditReq = new ReviewEditReq();
        reviewEditReq.setReviewId(reviewEditModel.getReviewId());
        reviewEditReq.setTitle(reviewEditModel.getTitle());
        reviewEditReq.setWriter(reviewEditModel.getWriter());
        reviewEditReq.setCategory(reviewEditModel.getCategory());
        reviewEditReq.setFileUrl(fileUrl);
        reviewEditReq.setFileName(originalFilename);
        reviewEditReq.setBody(reviewEditModel.getBody());
        reviewEditReq.setHashTags(reviewEditModel.getHashTags());

        return ResponseEntity.ok(new BaseResponse<>(reviewService.editReivew(reviewEditReq)));

    }


    @Operation(summary = "지원후기 정보 가져오기", description = """
            포인트가 부족하면
            \n
            "isSuccess": false,\n
            "code": 1001,\n
            "message": "포인트가 부족합니다."\n
            이 반환합니다. 
            \n
            로그인한 유저는 포인트 10포인트 차감되고 포인트 컬럼 생성됩니다.\n
            *주의* 지원후기 작성한 유저가 열람 할때는 포인트 차감이 되지 않습니다.\n
            이미 열랆한 자료실을 열어도 포인트 차감이 안됩니다.
            """)
    @GetMapping("/detail")
    public ResponseEntity<BaseResponse<ReviewDetailRes>> getReviewDetail(
            Principal principal,

            @Parameter(description = "지원후기아이디")
            @RequestParam(value = "reviewId", required = false)
            Long reviewId
    ) {
        ReviewDetailRes reviewDetail = reviewService.getReviewById(reviewId, principal.getName());
        return ResponseEntity.ok(new BaseResponse<>(reviewDetail));
    }

    @Operation(summary = "편집 버튼 눌렀을 때 사용자와 일치하는지 체크 및 정보 반환", description = """
            편집 버튼 눌르면 권한이 있는지 체크해준다.
            \n
            만약 있으면 기존 정보들을 반환한다.\n
            \n 권한이 없으면 아래 코드 처럼 나옴 \n
            {\n
              "isSuccess": false,\n
              "code": 1002,\n
              "message": "작성자가 아닙니다."\n
            }
            """)
    @GetMapping("/checkEdit")
    public ResponseEntity<BaseResponse<ReviewDetailRes>> getReviewEditable(
            Principal principal,

            @Parameter(description = "후 아이디")
            @RequestParam(value = "reviewId", required = false)
            Long reviewId
    ){
        if(principal == null ){
            throw new BaseException(BaseResponseStatus.NOT_WRITER);
        }
        return ResponseEntity.ok(new BaseResponse<>(this.reviewService.checkReviewEditable(reviewId, principal.getName())));
    }


    @Operation(summary = "지원후기 좋아요순으로 게시판 내용들 가져오기")
    @GetMapping("/sorted/likes")
    public ResponseEntity<BaseResponse<ReviewPageRes>> getAllReviewsSortedByLikes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes"));
        Page<Review> reviewPage = reviewService.getAllReviewsSortedByLikes(pageRequest);
        List<ReviewDtoRes> reviewDtos = reviewPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        ReviewPageRes reviewPageRes = new ReviewPageRes();
        reviewPageRes.setReviews(reviewDtos);
        reviewPageRes.setTotalSize(reviewPage.getTotalPages());

        return ResponseEntity.ok(new BaseResponse<>(reviewPageRes));
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