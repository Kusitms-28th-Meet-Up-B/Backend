package kusitms.gallae.service;

import kusitms.gallae.dto.program.ProgramMainRes;

import java.util.List;

public interface ProgramService {

    List<ProgramMainRes> getRecentPrograms();
}
