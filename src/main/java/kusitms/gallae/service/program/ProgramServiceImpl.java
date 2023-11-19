package kusitms.gallae.service.program;


import jakarta.transaction.Transactional;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.program.*;
import kusitms.gallae.dto.tourapi.TourApiDto;
import kusitms.gallae.global.DurationCalcurator;
import kusitms.gallae.domain.Program;
import kusitms.gallae.global.TourApiService;
import kusitms.gallae.global.jwt.JwtProvider;
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


    @Override
    public ProgramPageMainRes getProgramsByDynamicQuery(ProgramSearchReq programSearchReq,String username) {
        User user = null;
        if(username != null){
            user = userRepository.findById(Long.valueOf(username)).get();
            programSearchReq.setUser(user);
        }
        Page<ProgramMainRes> programs = programRepositoryCustom.getDynamicSearch(programSearchReq);
        List<ProgramMainRes> pageToListNewPrograms = programs.getContent();
        ProgramPageMainRes programPageMainRes = new ProgramPageMainRes();
        programPageMainRes.setPrograms(pageToListNewPrograms);
        programPageMainRes.setTotalSize(programs.getTotalPages());
        return programPageMainRes;
    }

    @Override
    public List<TourApiDto> getTourDatas(Long programId) {
        Program program = programRespository.findById(programId).orElse(null);
        return tourApiService.getTourDatas(program.getLocation());
    }

    @Override
    public List<ProgramMainRes> getSimilarPrograms(Long programId,String username) {
        Program program = programRespository.findById(programId).orElse(null);
        ProgramSimilarReq programSimilarReq = new ProgramSimilarReq();
        programSimilarReq.setLocation(program.getLocation());
        programSimilarReq.setProgramType(program.getProgramType());
        User user = null;
        if(username != null){
            user = userRepository.findById(Long.valueOf(username)).get();
            programSimilarReq.setUser(user);
        }
        List<ProgramMainRes> temp = programRepositoryCustom.getDynamicSimilar(programSimilarReq);
        List<ProgramMainRes> programs = new ArrayList<>();
        temp.stream().forEach(programMainRes -> {    //자기 자신은 추천안하게 제거
            if(programMainRes.getId() != programId) {
                programs.add(programMainRes);
            }
        });
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
    public ProgramDetailRes getProgramDetail(Long id){
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
            programMainRes.setHashTag(Arrays.stream(program.getHashTags().split(","))
                    .collect(Collectors.toList()));
            return programMainRes;
        }).collect(Collectors.toList());
    }

}
