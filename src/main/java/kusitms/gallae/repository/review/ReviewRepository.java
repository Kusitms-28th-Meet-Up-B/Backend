package kusitms.gallae.repository.review;

import kusitms.gallae.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review, Long> {



}
