package kusitms.gallae.service.review;
import kusitms.gallae.domain.Review;

import kusitms.gallae.domain.User;
import kusitms.gallae.dto.review.ReviewDtoRes;
import kusitms.gallae.dto.review.ReviewPageRes;
import kusitms.gallae.dto.review.ReviewPostReq;
import kusitms.gallae.repository.review.ReviewRepository;

import kusitms.gallae.repository.review.ReviewRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewRepositoryCustom reviewRepositoryCustom;


    public ReviewPageRes getReviewsByCategory(String category, Pageable pageable) {
        Page<Review> reviews = reviewRepositoryCustom.findReviewDynamicCategory(category,pageable);
        List<ReviewDtoRes> reviewDtos = reviews.getContent().stream()
                .map(review -> {
                    ReviewDtoRes reviewDtoRes =new ReviewDtoRes();
                    reviewDtoRes.setCategory(review.getCategory());
                    reviewDtoRes.setId(review.getId());
                    reviewDtoRes.setWriter(review.getWriter());
                    reviewDtoRes.setTitle(review.getTitle());
                    reviewDtoRes.setCreatedDate(review.getCreatedAt());
                    return reviewDtoRes;
                }).collect(Collectors.toList());

        ReviewPageRes reviewPageRes = new ReviewPageRes();
        reviewPageRes.setReviews(reviewDtos);
        reviewPageRes.setTotalSize(reviews.getTotalPages());

        return reviewPageRes;
    }

    public void postReivew(ReviewPostReq reviewPostReq) {
        Review review = new Review();
        User user = new User();
        user.setId(1L);
        review.setTitle(reviewPostReq.getTitle());
        review.setUser(user);
        review.setBody(reviewPostReq.getBody());
        review.setFileName(reviewPostReq.getFileName());
        review.setFileUrl(reviewPostReq.getFileUrl());
        review.setHashtag(reviewPostReq.getHashTags());
        reviewRepository.save(review);
    }
}
