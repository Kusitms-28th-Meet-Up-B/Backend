package kusitms.gallae.service.admin;

import kusitms.gallae.dto.program.*;

public interface ManagerService {


    Long postProgram(ProgramPostReq programPostReq,String username);

    Long postTempProgram(ProgramPostReq programPostReq,String username);

    ProgramPostReq getTempProgram(String username);

    ProgramDetailRes getProgramDetail(Long id);

    ProgramPageMangagerRes getManagerPrograms(ProgramManagerReq programManagerReq, String username);

    void deleteTempProgram(Long programId);

    void deleteProgram(Long programId);

    void finishProgram(Long programId);
}
