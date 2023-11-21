package kusitms.gallae.service.review;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.Point;
import kusitms.gallae.domain.Review;

import kusitms.gallae.domain.User;
import kusitms.gallae.domain.UserReview;
import kusitms.gallae.dto.review.ReviewDetailRes;
import kusitms.gallae.dto.review.ReviewDtoRes;
import kusitms.gallae.dto.review.ReviewPageRes;
import kusitms.gallae.dto.review.ReviewPostReq;
import kusitms.gallae.repository.favoriteReviewRepository.FavoriteReviewRepository;
import kusitms.gallae.repository.point.PointRepository;
import kusitms.gallae.repository.review.ReviewRepository;

import kusitms.gallae.repository.review.ReviewRepositoryCustom;
import kusitms.gallae.repository.user.UserRepository;
import kusitms.gallae.repository.userReview.UserReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @Autowired
    private FavoriteReviewRepository favoriteReviewRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private UserReviewRepository userReviewRepository;


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


    public Long postReivew(ReviewPostReq reviewPostReq,String username) {
        Review review = new Review();
        User user = userRepository.findById(Long.valueOf(username)).orElse(null);
        review.setTitle(reviewPostReq.getTitle());
        review.setUser(user);
        review.setBody(reviewPostReq.getBody());
        review.setCategory(reviewPostReq.getCategory());
        review.setFileName(reviewPostReq.getFileName());
        review.setWriter(reviewPostReq.getWriter());
        review.setFileUrl(reviewPostReq.getFileUrl());
        review.setHashtag(reviewPostReq.getHashTags());
        review.setLikes(0L);
        Review saveReview = reviewRepository.save(review);

        //포인트 적립
        Point point = new Point();
        point.setDate(LocalDate.now());
        point.setPointCategory("적립");
        point.setPointActivity("후기 작성");
        point.setTime(LocalTime.now());
        point.setPointScore(20);
        point.setUser(user);
        pointRepository.save(point);
        user.setPoint(user.getPoint() + 20);
        userRepository.save(user);
        return saveReview.getId();
    }


    public ReviewDetailRes getReviewById(Long reviewId, String username) {
        User user = userRepository.findById(Long.valueOf(username)).orElse(null);
        if(user.getPoint()<10) throw new BaseException(BaseResponseStatus.POINT_TRIBE);
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review.getUser().getId() != user.getId() && !userReviewRepository.existsByUserAndReview(user,review)) {
            //포인트 적립
            Point point = new Point();
            point.setDate(LocalDate.now());
            point.setPointCategory("사용");
            point.setPointActivity("후기 열람");
            point.setTime(LocalTime.now());
            point.setPointScore(-10);
            point.setUser(user);
            pointRepository.save(point);
            user.setPoint(user.getPoint() - 10);
            userRepository.save(user);
            UserReview userReview = new UserReview();
            userReview.setUser(user);
            userReview.setReview(review);
            userReviewRepository.save(userReview);
        }
        return convertReview(review,user);
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

    private ReviewDetailRes convertReview(Review review,User user){
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
        if( user != null) {
            detailRes.setLikeCheck(favoriteReviewRepository.existsByUserAndReview(user,review));
        }
        Long prevId = getPreviousReviewId(review.getId());
        Long nextId = getNextReviewId(review.getId());

        detailRes.setPreviousId(prevId);
        detailRes.setNextId(nextId);


        return detailRes;
    }
}
