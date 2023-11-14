package kusitms.gallae.service.program;

import kusitms.gallae.dto.program.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProgramService {

    ProgramDetailRes getProgramDetail(Long id);

    List<ProgramMainRes> getRecentPrograms();

    ProgramPageMainRes getProgramsByProgramType(String programType , Pageable pageable);

    ProgramPageMainRes getProgramsByProgramName(String programName , Pageable pageable);

    ProgramPageMainRes getProgramsByDynamicQuery(ProgramSearchReq programSearchReq);

    List<ProgramMainRes> getBestPrograms();

    List<ProgramMapRes> getProgramsMap();

    void postProgram(ProgramPostReq programPostReq);

    ProgramPostReq getTempProgram();

}
