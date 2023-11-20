package kusitms.gallae.service.review;
import kusitms.gallae.domain.Review;

import kusitms.gallae.domain.User;
import kusitms.gallae.dto.review.ReviewDetailRes;
import kusitms.gallae.dto.review.ReviewDtoRes;
import kusitms.gallae.dto.review.ReviewPageRes;
import kusitms.gallae.dto.review.ReviewPostReq;
import kusitms.gallae.repository.review.ReviewRepository;

import kusitms.gallae.repository.review.ReviewRepositoryCustom;
import kusitms.gallae.repository.user.UserRepository;
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

    @Autowired
    private UserRepository userRepository;


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


    public void postReivew(ReviewPostReq reviewPostReq,String username) {
        Review review = new Review();
        User user = userRepository.findById(Long.valueOf(username)).get();
        review.setTitle(reviewPostReq.getTitle());
        review.setUser(user);
        review.setBody(reviewPostReq.getBody());
        review.setCategory(review.getCategory());
        review.setFileName(reviewPostReq.getFileName());
        review.setWriter(reviewPostReq.getWriter());
        review.setFileUrl(reviewPostReq.getFileUrl());
        review.setHashtag(reviewPostReq.getHashTags());
        review.setLikes(0L);
        reviewRepository.save(review);
    }


    public ReviewDetailRes getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(review -> {
                    ReviewDetailRes detailRes = new ReviewDetailRes();
                    detailRes.setId(review.getId());
                    detailRes.setCategory(review.getCategory());
                    detailRes.setTitle(review.getTitle());
                    detailRes.setWriter(review.getWriter());
                    detailRes.setFileName(review.getFileName());
                    detailRes.setFileUrl(review.getFileUrl());
                    detailRes.setHashtag(review.getHashtag());
                    detailRes.setBody(review.getBody());
                    detailRes.setCreatedDate(review.getCreatedAt());

                    Long prevId = getPreviousReviewId(id);
                    Long nextId = getNextReviewId(id);

                    detailRes.setPreviousId(prevId);
                    detailRes.setNextId(nextId);


                    return detailRes;
                })
                .orElse(null);
    }

    public Long getPreviousReviewId(Long currentId) {
        return reviewRepository.findTop1ByIdLessThanOrderByIdDesc(currentId)
                .map(Review::getId)
                .orElse(null);
    }

    public Long getNextReviewId(Long currentId) {
        return reviewRepository.findTop1ByIdGreaterThanOrderByIdAsc(currentId)
                .map(Review::getId)
                .orElse(null);
    }

    public Page<Review> getAllReviewsSortedByLikes(Pageable pageable) {
        return reviewRepository.findAllByOrderByLikesDesc(pageable);
    }
}
