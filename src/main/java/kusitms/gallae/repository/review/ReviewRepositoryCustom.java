package kusitms.gallae.repository.review;

import kusitms.gallae.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
      Page<Review> findReviewDynamicCategory(String category, Pageable pageable);
}
