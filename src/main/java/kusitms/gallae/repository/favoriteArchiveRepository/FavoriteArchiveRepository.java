package kusitms.gallae.repository.favoriteArchiveRepository;

import kusitms.gallae.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteArchiveRepository extends JpaRepository<FavoriteArchive, Long> {

    Optional<FavoriteArchive> findByUserAndArchive(User user, Archive archive);
}
