package kusitms.gallae.repository.review;

import kusitms.gallae.domain.Review;
import kusitms.gallae.dto.user.UserPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findTop1ByIdLessThanOrderByIdDesc(Long id);
    Optional<Review> findTop1ByIdGreaterThanOrderByIdAsc(Long id);

    Page<Review> findAllByOrderByLikesDesc(Pageable pageable);
    Page<UserPostDto> findByWriter(String userId, Pageable pageable);
}