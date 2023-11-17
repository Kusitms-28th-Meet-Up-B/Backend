package kusitms.gallae.service.admin;

import kusitms.gallae.dto.program.ProgramDetailRes;
import kusitms.gallae.dto.program.ProgramPostReq;

public interface ManagerService {


    void postProgram(ProgramPostReq programPostReq);

    void postTempProgram(ProgramPostReq programPostReq);

    ProgramPostReq getTempProgram();

    ProgramDetailRes getProgramDetail(Long id);

    void deleteTempProgram(Long programId);
}
