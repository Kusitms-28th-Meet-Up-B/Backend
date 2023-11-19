package kusitms.gallae.repository.program;

import kusitms.gallae.domain.Program;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.dto.program.ProgramManagerReq;
import kusitms.gallae.dto.program.ProgramSearchReq;
import org.springframework.data.domain.Page;

public interface ProgramRepositoryCustom {

    Page<ProgramMainRes> getDynamicSearch(ProgramSearchReq programSearchReq);

    Page<Program> getDynamicMananger(ProgramManagerReq programManagerReq);
}
