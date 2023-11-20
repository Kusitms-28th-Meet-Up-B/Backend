package kusitms.gallae.service.favorite;


import jakarta.transaction.Transactional;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.*;
import kusitms.gallae.dto.favorite.FavoriteSearchReq;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.global.DurationCalcurator;
import kusitms.gallae.repository.archive.ArchiveRepository;
import kusitms.gallae.repository.favorite.FavoriteRepository;
import kusitms.gallae.repository.favorite.FavoriteRepositoryCustom;
import kusitms.gallae.repository.favoriteArchiveRepository.FavoriteArchiveRepository;
import kusitms.gallae.repository.favoriteReviewRepository.FavoriteReviewRepository;
import kusitms.gallae.repository.program.ProgramRespository;
import kusitms.gallae.repository.review.ReviewRepository;
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

    @Autowired
    private FavoriteReviewRepository favoriteReviewRepository;

    @Autowired
    private FavoriteArchiveRepository favoriteArchiveRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    @Override
    public void postFavorite(String username, Long programId) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        Program program = programRespository.findById(programId).get();
        Favorite favorite = favoriteRepository.findByUserAndProgram(user,program).orElse(null);
        if(favorite == null ) {
            program.setProgramLike(program.getProgramLike() + 1);
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setProgram(program);
            favoriteRepository.save(newFavorite);
        }else{
            favoriteRepository.delete(favorite);
        }
    }

    @Override
    public void postFavoriteReview(String username, Long reviewId) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        Review review = reviewRepository.findById(reviewId).get();
        FavoriteReview favoriteReview = favoriteReviewRepository.findByUserAndReview(user,review).orElse(null);
        if(favoriteReview == null ) {
            review.setLikes(review.getLikes() + 1);
            FavoriteReview newFavoriteReview = new FavoriteReview();
            newFavoriteReview.setUser(user);
            newFavoriteReview.setReview(review);
            favoriteReviewRepository.save(newFavoriteReview);
        }else{
            favoriteReviewRepository.delete(favoriteReview);
        }
    }

    @Override
    public void postFavoriteArchive(String username, Long archiveId) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        Archive archive = archiveRepository.findById(archiveId).get();
        FavoriteArchive favoriteArchive = favoriteArchiveRepository.findByUserAndArchive(user,archive).orElse(null);
        if(favoriteArchive == null ) {
            archive.setLikes(archive.getLikes() + 1);
            FavoriteArchive newFavoriteArchive = new FavoriteArchive();
            newFavoriteArchive.setUser(user);
            newFavoriteArchive.setArchive(archive);
            favoriteArchiveRepository.save(newFavoriteArchive);
        }else{
            favoriteArchiveRepository.delete(favoriteArchive);
        }
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
