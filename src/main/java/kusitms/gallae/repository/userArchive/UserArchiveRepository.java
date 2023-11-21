package kusitms.gallae.repository.userArchive;

import kusitms.gallae.domain.Archive;
import kusitms.gallae.domain.Review;
import kusitms.gallae.domain.User;
import kusitms.gallae.domain.UserArchive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserArchiveRepository extends JpaRepository<UserArchive,Long> {

    boolean existsByUserAndArchive(User user, Archive archive);

    void deleteAllByArchive(Archive archive);
}
