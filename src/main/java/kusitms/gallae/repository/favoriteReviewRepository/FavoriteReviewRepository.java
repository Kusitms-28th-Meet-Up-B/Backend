package kusitms.gallae.repository.favoriteReviewRepository;

import kusitms.gallae.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteReviewRepository extends JpaRepository<FavoriteReview, Long> {

    Optional<FavoriteReview> findByUserAndReview(User user, Review review);

    boolean existsByUserAndReview(User user, Review review);

    void deleteAllByReview(Review review);
}
