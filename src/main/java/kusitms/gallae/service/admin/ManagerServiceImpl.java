package kusitms.gallae.service.admin;

import jakarta.transaction.Transactional;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.model.PostModel;
import kusitms.gallae.dto.model.PostModelGet;
import kusitms.gallae.dto.program.*;
import kusitms.gallae.global.DurationCalcurator;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.global.jwt.AuthUtil;
import kusitms.gallae.repository.favorite.FavoriteRepository;
import kusitms.gallae.repository.program.ProgramRepositoryCustom;
import kusitms.gallae.repository.program.ProgramRepositoryImpl;
import kusitms.gallae.repository.program.ProgramRespository;
import kusitms.gallae.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private final ProgramRespository programRespository;

    private final ProgramRepositoryCustom programRepositoryCustom;

    private final S3Service s3Service;

    private final UserRepository userRepository;

    private final FavoriteRepository favoriteRepository;


    @Override
    public PostModelGet getProgramDetail(Long id){
        Program program = programRespository.findById(id).orElse(null);
        if(program == null) {
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }else{
            PostModelGet postModel = new PostModelGet();
            postModel.setProgramName(program.getProgramName());
            postModel.setLocation(program.getLocation());
            postModel.setProgramType(program.getProgramType());
            postModel.setProgramDetailType(program.getDetailType());
            postModel.setRecruitStartDate(program.getRecruitStartDate());
            postModel.setRecruitEndDate(program.getRecruitEndDate());
            postModel.setActiveStartDate(program.getActiveStartDate());
            postModel.setActiveEndDate(program.getActiveEndDate());
            postModel.setContact(program.getContact());
            postModel.setContactPhone(program.getContactNumber());
            postModel.setLink(program.getProgramLink());
            postModel.setHashtag(program.getHashTags());
            postModel.setBody(program.getDescription());
            postModel.setPhoto(program.getPhotoUrl());

            return postModel;
        }
    }

    @Override
    public ProgramPostReq getTempProgram(String username) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        Program tempProgram = programRespository.findByUserIdAndStatus(user.getId(), Program.ProgramStatus.TEMP);
        if(tempProgram == null) {
            return new ProgramPostReq();
        }else{
            ProgramPostReq programPostReq = new ProgramPostReq();
            programPostReq.setProgramId(tempProgram.getId());
            programPostReq.setProgramName(tempProgram.getProgramName());
            programPostReq.setProgramType(tempProgram.getProgramType());
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
    public Long postProgram(ProgramPostReq programPostReq ,String username) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        Program tempProgram = programRespository.findByUserIdAndStatus(user.getId(),  //나중에 유저 생기면 수정 필요
                Program.ProgramStatus.TEMP);

        if(tempProgram == null) { //임시 저장이 없으면
            Program program = new Program();
            Program saveProgram = this.getProgramEntity(program,programPostReq);
            saveProgram.setUser(user);
            saveProgram.setStatus(Program.ProgramStatus.SAVE);
            Program programId = programRespository.save(saveProgram);
            return programId.getId();
        }else {      //임시 저장이 있으면
            Program saveProgram = this.getProgramEntity(tempProgram, programPostReq);
            saveProgram.setStatus(Program.ProgramStatus.SAVE);
            return tempProgram.getId();
        }
    }

    @Override
    public Long editProgram(ProgramPostReq programPostReq ,String username) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        Program tempProgram = programRespository.findById(programPostReq.getProgramId()).orElse(null);
        if(user.getId() != tempProgram.getUser().getId()) {
            throw new BaseException(BaseResponseStatus.NOT_WRITER);
        }

        Program saveProgram = this.getProgramEntity(tempProgram,programPostReq);
        saveProgram.setStatus(Program.ProgramStatus.SAVE);
        Program programId = programRespository.save(saveProgram);
        return programId.getId();
    }

    @Override
    public Long postTempProgram(ProgramPostReq programPostReq , String username) {
        User user = userRepository.findById(Long.valueOf(username)).get();
        Program tempProgram = programRespository.findByUserIdAndStatus(user.getId(),  //나중에 유저 생기면 수정 필요
                Program.ProgramStatus.TEMP);

        if(tempProgram == null) { //임시 저장이 없으면
            Program program = new Program();
            Program saveProgram = this.getProgramEntity(program,programPostReq);
            saveProgram.setUser(user);
            saveProgram.setStatus(Program.ProgramStatus.TEMP);
            Program programId = programRespository.save(saveProgram);
            return programId.getId();
        }else {      //임시 저장이 있으면
            Program saveProgram = this.getProgramEntity(tempProgram, programPostReq);
            saveProgram.setStatus(Program.ProgramStatus.TEMP);
            return saveProgram.getId();
        }
    }

    @Override
    @Transactional
    public void deleteTempProgram(Long programId) {
        Program program = programRespository.findById(programId).orElse(null);
        favoriteRepository.deleteAllByProgram(program);
        programRespository.deleteByIdAndStatus(programId, Program.ProgramStatus.TEMP);
    }

    @Override
    public ProgramPageMangagerRes getManagerPrograms(ProgramManagerReq programManagerReq , String username) {
        Optional<User> user = userRepository.findById(Long.valueOf(username));
        programManagerReq.setUser(user.get());
        Page<Program> programs = programRepositoryCustom.getDynamicMananger(programManagerReq);
        List<Program> pageToListManagerPrograms = programs.getContent();
        ProgramPageMangagerRes programPageMangagerRes = new ProgramPageMangagerRes();
        programPageMangagerRes.setPrograms(getProgramManagerRes(pageToListManagerPrograms));
        programPageMangagerRes.setTotalSize(programs.getTotalPages());
        return programPageMangagerRes;
    }

    @Override
    public void finishProgram(Long programId) {
        Program program = programRespository.findById(programId).orElse(null);
        program.setStatus(Program.ProgramStatus.FINISH);
    }

    @Override
    @Transactional
    public void deleteProgram(Long programId) {
        Program program = programRespository.findById(programId).orElse(null);
        favoriteRepository.deleteAllByProgram(program);
        programRespository.deleteById(programId);
    }

    private List<ProgramManagerRes> getProgramManagerRes(List<Program> programs){
        return programs.stream().map(program -> {
            ProgramManagerRes programManagerRes = new ProgramManagerRes();
            programManagerRes.setId(program.getId());
            programManagerRes.setTitle(program.getProgramName());
            programManagerRes.setLike(program.getProgramLike());
            programManagerRes.setViewCount(program.getViewCount());
            programManagerRes.setRecruitStartDate(program.getRecruitStartDate());
            programManagerRes.setRecuritEndDate(program.getRecruitEndDate());
            return programManagerRes;
        }).collect(Collectors.toList());
    }

    private Program getProgramEntity(Program program ,ProgramPostReq programPostReq) {
        program.setProgramName(programPostReq.getProgramName());
        if(program.getPhotoUrl()==null) {
            program.setPhotoUrl(programPostReq.getPhotoUrl());
        }else{
            s3Service.deleteFile(program.getPhotoUrl());
            program.setPhotoUrl(program.getPhotoUrl());
        }
        program.setLocation(programPostReq.getLocation());
        program.setRecruitStartDate(programPostReq.getRecruitStartDate());
        program.setRecruitEndDate(programPostReq.getRecruitEndDate());
        program.setActiveStartDate(programPostReq.getActiveStartDate());
        program.setActiveEndDate(programPostReq.getActiveEndDate());
        program.setContact(programPostReq.getContact());
        program.setContactNumber(programPostReq.getContactPhone());
        program.setProgramType(programPostReq.getProgramType());
        program.setDetailType(programPostReq.getProgramDetailType());
        program.setProgramLink(programPostReq.getLink());
        program.setHashTags(programPostReq.getHashtag());
        program.setDescription(programPostReq.getBody());
        program.setProgramLike(0L);
        program.setViewCount(0L);
        return program;
    }
}
