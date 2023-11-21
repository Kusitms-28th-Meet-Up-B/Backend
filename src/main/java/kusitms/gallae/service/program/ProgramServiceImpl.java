package kusitms.gallae.service.program;


import jakarta.transaction.Transactional;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.Favorite;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.program.*;
import kusitms.gallae.dto.tourapi.TourApiDto;
import kusitms.gallae.global.DurationCalcurator;
import kusitms.gallae.domain.Program;
import kusitms.gallae.global.TourApiService;
import kusitms.gallae.global.jwt.JwtProvider;
import kusitms.gallae.repository.favorite.FavoriteRepository;
import kusitms.gallae.repository.program.ProgramRepositoryCustom;
import kusitms.gallae.repository.program.ProgramRepositoryImpl;
import kusitms.gallae.repository.program.ProgramRespository;
import kusitms.gallae.repository.user.UserRepository;
import kusitms.gallae.service.program.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProgramServiceImpl implements ProgramService {
    private final ProgramRespository programRespository;

    private final ProgramRepositoryCustom programRepositoryCustom;

    private final UserRepository userRepository;

    private final TourApiService tourApiService;

    private final FavoriteRepository favoriteRepository;


    @Override
    public ProgramPageMainRes getProgramsByDynamicQuery(ProgramSearchReq programSearchReq,String username) {
        User user = null;
        if(username != null){
            user = userRepository.findById(Long.valueOf(username)).get();
            programSearchReq.setUser(user);
        }
        Page<Program> temp = programRepositoryCustom.getDynamicSearch(programSearchReq);
        List<Program> pageToListNewPrograms = temp.getContent();
        List<ProgramMainRes> programs = new ArrayList<>();
        if(user == null ){
            programs = getProgramMainRes(pageToListNewPrograms);
        }else{
            programs = getProgramMainRes(pageToListNewPrograms,user);
        }
        ProgramPageMainRes programPageMainRes = new ProgramPageMainRes();
        programPageMainRes.setPrograms(programs);
        programPageMainRes.setTotalSize(temp.getTotalPages());
        return programPageMainRes;
    }

    @Override
    public List<TourApiDto> getTourDatas(Long programId) {
        Program program = programRespository.findById(programId).orElse(null);
        return tourApiService.getTourDatas(program.getLocation());
    }

    @Override
    public List<TourApiDto> getTourLodgment(Long programId) {
        Program program = programRespository.findById(programId).orElse(null);
        return tourApiService.getTourLodgment(program.getLocation());
    }

    @Override
    public List<ProgramMainRes> getSimilarPrograms(Long programId,String username) {
        Program program = programRespository.findById(programId).orElse(null);
        ProgramSimilarReq programSimilarReq = new ProgramSimilarReq();
        programSimilarReq.setLocation(program.getLocation());
        programSimilarReq.setProgramType(program.getProgramType());
        programSimilarReq.setId(programId);
        User user = null;
        if(username != null){
            user = userRepository.findById(Long.valueOf(username)).orElse(null);
        }
        List<Program> temp = programRepositoryCustom.getDynamicSimilar(programSimilarReq);
        List<ProgramMainRes> programs = new ArrayList<>();
        if(user == null ){
            programs = getProgramMainRes(temp);
        }else{
            programs = getProgramMainRes(temp,user);
        }
        return programs;
    }

    @Override
    public List<ProgramMapRes> getProgramsMap(){
        List<Program> programs = programRespository.findAll();  //나중에 수정필요

        return programs.stream().map(program -> {
            ProgramMapRes programMapRes = new ProgramMapRes();
            programMapRes.setId(program.getId());
            programMapRes.setProgramName(program.getProgramName());
            programMapRes.setLongitude(program.getLongitude());
            programMapRes.setLatitude(program.getLatitude());
            programMapRes.setPhotoUrl(program.getPhotoUrl());
            LocalDate startLocalDate = LocalDate.of(program.getRecruitStartDate().getYear(),
                    program.getRecruitStartDate().getMonth(),program.getRecruitStartDate().getDayOfMonth());
            programMapRes.setRecruitStartDate(startLocalDate);
            LocalDate endLocalDate = LocalDate.of(program.getRecruitEndDate().getYear(),
                    program.getRecruitEndDate().getMonth(),program.getRecruitEndDate().getDayOfMonth());
            programMapRes.setRecruitEndDate(endLocalDate);

            return programMapRes;
        }).collect(Collectors.toList());
    }

    @Override
    public ProgramDetailRes getProgramDetail(Long id, String username){
        Program program = programRespository.findById(id).orElse(null);
        program.setViewCount(program.getViewCount()+1);

        if(program == null) {
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }else{
            ProgramDetailRes programDetailRes = new ProgramDetailRes();
            programDetailRes.setId(program.getId());
            programDetailRes.setProgramName(program.getProgramName());
            programDetailRes.setProgramLink(program.getProgramLink());
            programDetailRes.setContact(program.getContact());
            programDetailRes.setContactNumber(program.getContactNumber());
            programDetailRes.setDescription(program.getDescription());
            programDetailRes.setLocation(program.getLocation());
            programDetailRes.setActiveStartDate(program.getActiveStartDate());
            programDetailRes.setActiveEndDate(program.getActiveEndDate());
            programDetailRes.setRecruitStartDate(program.getRecruitStartDate());
            programDetailRes.setRecruitEndDate(program.getRecruitEndDate());
            programDetailRes.setTripStartDate(program.getTripStartDate());
            programDetailRes.setTripEndDate(program.getTripEndDate());
            programDetailRes.setLike(program.getProgramLike());
            User user = null;
            if(username != null){
                user = userRepository.findById(Long.valueOf(username)).orElse(null);
                if(user !=null && favoriteRepository.existsByUserAndProgram(user,program)){
                    programDetailRes.setUserLikeCheck(true);
                }
            }
            programDetailRes.setWriterId(program.getUser().getId());
            programDetailRes.setPhotoUrl(program.getPhotoUrl());
            LocalDate localDate = LocalDate.of(program.getRecruitEndDate().getYear(),
                    program.getRecruitEndDate().getMonth(),program.getRecruitEndDate().getDayOfMonth());
            String strRemainDay = DurationCalcurator.getDuration(localDate);
            programDetailRes.setRemainDay(strRemainDay);
            return programDetailRes;
        }
    }


    private List<ProgramMainRes> getProgramMainRes(List<Program> programs){
        return programs.stream().map(program -> {
            ProgramMainRes programMainRes = new ProgramMainRes();
            programMainRes.setId(program.getId());
            programMainRes.setProgramName(program.getProgramName());
            programMainRes.setLike(program.getProgramLike());
            programMainRes.setPhotoUrl(program.getPhotoUrl());
            LocalDate localDate = LocalDate.of(program.getRecruitEndDate().getYear(),
                    program.getRecruitEndDate().getMonth(),program.getRecruitEndDate().getDayOfMonth());
            String strRemainDay = DurationCalcurator.getDuration(localDate);
            programMainRes.setRemainDay(strRemainDay);
            programMainRes.setRecruitStartDate(program.getRecruitStartDate());
            programMainRes.setRecruitEndDate(program.getRecruitEndDate());
            programMainRes.setHashTag(Arrays.stream(program.getHashTags().split(","))
                    .collect(Collectors.toList()));
            return programMainRes;
        }).collect(Collectors.toList());
    }

    private List<ProgramMainRes> getProgramMainRes(List<Program> programs , User u){
        return programs.stream().map(p -> {
            ProgramMainRes programMainRes = new ProgramMainRes();
            programMainRes.setId(p.getId());
            programMainRes.setProgramName(p.getProgramName());
            programMainRes.setLike(p.getProgramLike());
            programMainRes.setPhotoUrl(p.getPhotoUrl());
            programMainRes.setLatitude(p.getLatitude());
            programMainRes.setLongitude(p.getLongitude());
            LocalDate localDate = LocalDate.of(p.getRecruitEndDate().getYear(),
                    p.getRecruitEndDate().getMonth(),p.getRecruitEndDate().getDayOfMonth());
            String strRemainDay = DurationCalcurator.getDuration(localDate);
            programMainRes.setRemainDay(strRemainDay);
            programMainRes.setRecruitStartDate(p.getRecruitStartDate());
            programMainRes.setRecruitEndDate(p.getRecruitEndDate());
            programMainRes.setHashTag(Arrays.stream(p.getHashTags().split(","))
                    .collect(Collectors.toList()));
            if(favoriteRepository.existsByUserAndProgram(u,p)){
                programMainRes.setUserLikeCheck(true);
            }
            return programMainRes;
        }).collect(Collectors.toList());
    }

}
