package kusitms.gallae.service.admin;

import kusitms.gallae.dto.program.*;

public interface ManagerService {


    void postProgram(ProgramPostReq programPostReq);

    void postTempProgram(ProgramPostReq programPostReq);

    ProgramPostReq getTempProgram();

    ProgramDetailRes getProgramDetail(Long id);

    ProgramPageMangagerRes getManagerPrograms(ProgramManagerReq programManagerReq);

    void deleteTempProgram(Long programId);

    void deleteProgram(Long programId);

    void finishProgram(Long programId);
}
