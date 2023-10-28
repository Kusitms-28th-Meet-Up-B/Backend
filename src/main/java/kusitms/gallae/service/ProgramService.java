package kusitms.gallae.service;

import kusitms.gallae.dto.program.ProgramDetailRes;
import kusitms.gallae.dto.program.ProgramMainRes;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProgramService {

    ProgramDetailRes getProgramDetail(Long id);

    List<ProgramMainRes> getRecentPrograms();

    List<ProgramMainRes> getProgramsByProgramType(String programType , Pageable pageable);

    List<ProgramMainRes> getBestPrograms();
}
