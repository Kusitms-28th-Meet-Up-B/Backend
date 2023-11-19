package kusitms.gallae.repository.favorite;

import kusitms.gallae.domain.Favorite;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserAndProgram(User user, Program program);
}
