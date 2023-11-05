package kusitms.gallae.service;

import kusitms.gallae.dto.program.ProgramDetailRes;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.dto.program.ProgramMapRes;
import kusitms.gallae.dto.program.ProgramPageMainRes;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProgramService {

    ProgramDetailRes getProgramDetail(Long id);

    List<ProgramMainRes> getRecentPrograms();

    ProgramPageMainRes getProgramsByProgramType(String programType , Pageable pageable);

    ProgramPageMainRes getProgramsByProgramName(String programName , Pageable pageable);

    List<ProgramMainRes> getBestPrograms();

    List<ProgramMapRes> getProgramsMap();


}
