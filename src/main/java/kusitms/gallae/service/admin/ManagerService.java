package kusitms.gallae.service.admin;

import kusitms.gallae.dto.program.ProgramPostReq;

public interface ManagerService {


    void postProgram(ProgramPostReq programPostReq);

    void postTempProgram(ProgramPostReq programPostReq);

    ProgramPostReq getTempProgram();

    void deleteTempProgram(Long programId);
}
