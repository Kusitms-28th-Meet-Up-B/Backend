package kusitms.gallae.repository.archive;

import kusitms.gallae.domain.Archive;

import kusitms.gallae.domain.Review;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.user.UserPostDto;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    Optional<Archive> findTop1ByIdLessThanOrderByIdDesc(Long id);
    Optional<Archive> findTop1ByIdGreaterThanOrderByIdAsc(Long id);
    Page<Archive> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Archive> findAllByOrderByLikesDesc(Pageable pageable);


}
