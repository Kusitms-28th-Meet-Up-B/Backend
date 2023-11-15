package kusitms.gallae.service.program;


import jakarta.transaction.Transactional;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.program.*;
import kusitms.gallae.global.DurationCalcurator;
import kusitms.gallae.domain.Program;
import kusitms.gallae.global.jwt.JwtProvider;
import kusitms.gallae.repository.program.ProgramRepositoryImpl;
import kusitms.gallae.repository.program.ProgramRespository;
import kusitms.gallae.service.program.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProgramServiceImpl implements ProgramService {
    private final ProgramRespository programRespository;

    private final ProgramRepositoryImpl programRepositoryImpl;

    private final JwtProvider jwtProvider;

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
    public void deleteTempProgram(Long programId) {
        programRespository.deleteById(programId);
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

    @Override
    public ProgramPostReq getTempProgram() {
        Program tempProgram = programRespository.findByUserIdAndStatus(1L, Program.ProgramStatus.TEMPSAVE);
        if(tempProgram == null) {
            return new ProgramPostReq();
        }else{
            ProgramPostReq programPostReq = new ProgramPostReq();
            programPostReq.setProgramId(tempProgram.getId());
            programPostReq.setProgramName(tempProgram.getProgramName());
            programPostReq.setPhotoUrl(tempProgram.getPhotoUrl());
            programPostReq.setLocation(tempProgram.getLocation());
            programPostReq.setRecruitStartDate(tempProgram.getRecruitStartDate());
            programPostReq.setRecruitEndDate(tempProgram.getRecruitEndDate());
            programPostReq.setActiveStartDate(tempProgram.getActiveStartDate());
            programPostReq.setActiveEndDate(tempProgram.getActiveEndDate());
            programPostReq.setContact(tempProgram.getContact());
            programPostReq.setContactPhone(tempProgram.getContactNumber());
            programPostReq.setProgramDetailType(tempProgram.getDetailType());
            programPostReq.setLink(tempProgram.getProgramLink());
            programPostReq.setHashtag(tempProgram.getHashTags());
            programPostReq.setBody(tempProgram.getDescription());
            return programPostReq;
        }
    }
    @Override
    public void postProgram(ProgramPostReq programPostReq) {
        Program tempProgram = programRespository.findByUserIdAndStatus(1L,  //나중에 유저 생기면 수정 필요
                Program.ProgramStatus.TEMPSAVE);
        System.out.println(tempProgram);
        User user = new User();
        user.setId(1L);
        if(tempProgram == null) { //임시 저장이 없으면
            Program program = new Program();
            Program saveProgram = this.getProgramEntity(program,programPostReq);
            program.setUser(user);
            programRespository.save(saveProgram);
        }else {      //임시 저장이 있으면
            Program saveProgram = this.getProgramEntity(tempProgram, programPostReq);
            programRespository.save(saveProgram);
        }
    }

    private Program getProgramEntity(Program program ,ProgramPostReq programPostReq) {
        program.setProgramName(programPostReq.getProgramName());
        program.setPhotoUrl(programPostReq.getPhotoUrl());
        program.setLocation(programPostReq.getLocation());
        program.setRecruitStartDate(programPostReq.getRecruitStartDate());
        program.setRecruitEndDate(programPostReq.getRecruitEndDate());
        program.setActiveStartDate(programPostReq.getActiveStartDate());
        program.setActiveEndDate(programPostReq.getActiveEndDate());
        program.setContact(programPostReq.getContact());
        program.setContactNumber(programPostReq.getContactPhone());
        program.setDetailType(programPostReq.getProgramDetailType());
        program.setProgramLink(programPostReq.getLink());
        program.setHashTags(programPostReq.getHashtag());
        program.setDescription(programPostReq.getBody());
        program.setProgramLike(0L);
        program.setViewCount(0L);
        program.setStatus(Program.ProgramStatus.SAVE);
        return program;
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
