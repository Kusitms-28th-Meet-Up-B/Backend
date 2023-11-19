package kusitms.gallae.service.program;

import kusitms.gallae.dto.program.*;
import kusitms.gallae.dto.tourapi.TourApiDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProgramService {

    ProgramDetailRes getProgramDetail(Long id);

    ProgramPageMainRes getProgramsByDynamicQuery(ProgramSearchReq programSearchReq, String username);

    List<ProgramMapRes> getProgramsMap();

    List<ProgramMainRes> getSimilarPrograms(Long ProgramId,String username);

    List<TourApiDto> getTourDatas(Long programId);

}
