package kusitms.gallae.service.favorite;


import jakarta.transaction.Transactional;
import kusitms.gallae.domain.Favorite;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.favorite.FavoriteSearchReq;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.global.DurationCalcurator;
import kusitms.gallae.repository.favorite.FavoriteRepository;
import kusitms.gallae.repository.favorite.FavoriteRepositoryCustom;
import kusitms.gallae.repository.program.ProgramRespository;
import kusitms.gallae.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private FavoriteRepositoryCustom favoriteRepositoryCustom;

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

    @Override
    public List<ProgramMainRes> getFavoriteByUser(String username, FavoriteSearchReq favoriteSearchReq) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        favoriteSearchReq.setUser(user);
        List<Program> favorites = favoriteRepositoryCustom.getFavoritePrograms(favoriteSearchReq);
        return favorites.stream().map(program -> {
            ProgramMainRes programMainRes = new ProgramMainRes();
            programMainRes.setId(program.getId());
            programMainRes.setProgramName(program.getProgramName());
            programMainRes.setPhotoUrl(program.getPhotoUrl());
            programMainRes.setLike(program.getProgramLike());
            LocalDate localDate = LocalDate.of(program.getRecruitEndDate().getYear(),
                    program.getRecruitEndDate().getMonth(),program.getRecruitEndDate().getDayOfMonth());
            String strRemainDay = DurationCalcurator.getDuration(localDate);
            programMainRes.setRemainDay(strRemainDay);
            programMainRes.setHashTag(Arrays.stream(program.getHashTags().split(","))
                    .collect(Collectors.toList()));
            programMainRes.setUserLikeCheck(true);
            return programMainRes;
        }).collect(Collectors.toList());
    }
}
