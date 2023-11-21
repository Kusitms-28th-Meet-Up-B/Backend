package kusitms.gallae.repository.userReview;

import kusitms.gallae.domain.Review;
import kusitms.gallae.domain.User;
import kusitms.gallae.domain.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReviewRepository extends JpaRepository<UserReview,Long> {

    boolean existsByUserAndReview(User user, Review review);

    void deleteAllByReview(Review review);
}
