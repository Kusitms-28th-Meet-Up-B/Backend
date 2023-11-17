package kusitms.gallae.repository.user;


import kusitms.gallae.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickName(String nickName);
    Optional<User> findByLoginId(String loginId);
}
