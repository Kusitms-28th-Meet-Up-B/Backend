package kusitms.gallae.service.admin;

import jakarta.transaction.Transactional;
import kusitms.gallae.domain.Program;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.program.ProgramPostReq;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.repository.program.ProgramRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private final ProgramRespository programRespository;

    private final S3Service s3Service;

    @Override
    public ProgramPostReq getTempProgram() {
        Program tempProgram = programRespository.findByUserIdAndStatus(1L, Program.ProgramStatus.TEMP);
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
                Program.ProgramStatus.TEMP);
        System.out.println(tempProgram);
        User user = new User();
        user.setId(1L);
        if(tempProgram == null) { //임시 저장이 없으면
            Program program = new Program();
            Program saveProgram = this.getProgramEntity(program,programPostReq);
            saveProgram.setUser(user);
            saveProgram.setStatus(Program.ProgramStatus.SAVE);
            programRespository.save(saveProgram);
        }else {      //임시 저장이 있으면
            Program saveProgram = this.getProgramEntity(tempProgram, programPostReq);
            saveProgram.setStatus(Program.ProgramStatus.SAVE);
        }
    }

    @Override
    public void postTempProgram(ProgramPostReq programPostReq) {
        Program tempProgram = programRespository.findByUserIdAndStatus(1L,  //나중에 유저 생기면 수정 필요
                Program.ProgramStatus.TEMP);
        System.out.println(tempProgram);
        User user = new User();
        user.setId(1L);
        if(tempProgram == null) { //임시 저장이 없으면
            Program program = new Program();
            Program saveProgram = this.getProgramEntity(program,programPostReq);
            saveProgram.setUser(user);
            saveProgram.setStatus(Program.ProgramStatus.TEMP);
            programRespository.save(saveProgram);
        }else {      //임시 저장이 있으면
            Program saveProgram = this.getProgramEntity(tempProgram, programPostReq);
            saveProgram.setStatus(Program.ProgramStatus.TEMP);
        };
    }

    @Override
    public void deleteTempProgram(Long programId) {
        programRespository.deleteById(programId);
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
        program.setDetailType(programPostReq.getProgramDetailType());
        program.setProgramLink(programPostReq.getLink());
        program.setHashTags(programPostReq.getHashtag());
        program.setDescription(programPostReq.getBody());
        program.setProgramLike(0L);
        program.setViewCount(0L);
        return program;
    }
}
