package kusitms.gallae.service.favorite;


import jakarta.transaction.Transactional;
import kusitms.gallae.domain.Favorite;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.User;
import kusitms.gallae.repository.favorite.FavoriteRepository;
import kusitms.gallae.repository.program.ProgramRespository;
import kusitms.gallae.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgramRespository programRespository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public void postFavorite(String username, Long programId) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        Program program = programRespository.findById(programId).get();
        program.setProgramLike(program.getProgramLike()+1);
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProgram(program);
        favoriteRepository.save(favorite);
    }
}
