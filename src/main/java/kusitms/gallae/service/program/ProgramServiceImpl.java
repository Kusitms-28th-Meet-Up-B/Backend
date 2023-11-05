package kusitms.gallae.service.program;


import jakarta.transaction.Transactional;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.dto.program.*;
import kusitms.gallae.global.DurationCalcurator;
import kusitms.gallae.domain.Program;
import kusitms.gallae.repository.program.ProgramRepositoryImpl;
import kusitms.gallae.repository.program.ProgramRespository;
import kusitms.gallae.service.program.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProgramServiceImpl implements ProgramService {
    private final ProgramRespository programRespository;

    private final ProgramRepositoryImpl programRepositoryImpl;

    @Override
    public List<ProgramMainRes> getRecentPrograms(){
        List<Program> programs = programRespository.findTop4ByOrderByCreatedAtDesc();

        return getProgramMainRes(programs);
    }

    @Override
    public ProgramPageMainRes getProgramsByProgramType(String programType, Pageable pageable) {
        Page<Program> programs = programRespository.findAllByProgramTypeOrderByCreatedAtDesc(programType , pageable);
        List<Program> pageToListNewPrograms = programs.getContent();
        ProgramPageMainRes programPageMainRes = new ProgramPageMainRes();
        programPageMainRes.setPrograms(getProgramMainRes(pageToListNewPrograms));
        programPageMainRes.setTotalSize(programs.getTotalPages());
        return programPageMainRes;
    }

    public ProgramPageMainRes getProgramsByProgramName(String programName, Pageable pageable) {
        Page<Program> programs = programRespository.findProgramByProgramNameContaining(programName , pageable);
        List<Program> pageToListNewPrograms = programs.getContent();
        ProgramPageMainRes programPageMainRes = new ProgramPageMainRes();
        programPageMainRes.setPrograms(getProgramMainRes(pageToListNewPrograms));
        programPageMainRes.setTotalSize(programs.getTotalPages());
        return programPageMainRes;
    }

    @Override
    public ProgramPageMainRes getProgramsByDynamicQuery(ProgramSearchReq programSearchReq) {
        Page<Program> programs = programRepositoryImpl.getDynamicSearch(programSearchReq);
        List<Program> pageToListNewPrograms = programs.getContent();
        ProgramPageMainRes programPageMainRes = new ProgramPageMainRes();
        programPageMainRes.setPrograms(getProgramMainRes(pageToListNewPrograms));
        programPageMainRes.setTotalSize(programs.getTotalPages());
        return programPageMainRes;
    }

    @Override
    public List<ProgramMainRes> getBestPrograms(){
        List<Program> programs = programRespository.findTop4ByOrderByProgramLikeDesc();
        return getProgramMainRes(programs);
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
            programMapRes.setRecruitStartDate(program.getRecruitStartDate());
            programMapRes.setRecruitEndDate(program.getRecruitEndDate());
            return programMapRes;
        }).collect(Collectors.toList());
    }


    @Override
    public ProgramDetailRes getProgramDetail(Long id){
        Program program = programRespository.findById(id).orElse(null);
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
            String strRemainDay = DurationCalcurator.getDuration(program.getRecruitEndDate());
            programMainRes.setRemainDay(strRemainDay);
            programMainRes.setHashTag(Arrays.stream(program.getHashTags().split(","))
                    .collect(Collectors.toList()));
            return programMainRes;
        }).collect(Collectors.toList());
    }

}
