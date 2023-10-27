package kusitms.gallae.service;


import jakarta.transaction.Transactional;
import kusitms.gallae.global.DurationCalcurator;
import kusitms.gallae.domain.Program;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.repository.ProgramRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProgramServiceImpl implements ProgramService {
    private final ProgramRespository programRespository;

    @Override
    public List<ProgramMainRes> getRecentPrograms(){
        List<Program> programs = programRespository.findTop4ByOrderByCreatedAtDesc();

        return programs.stream().map(program -> {
            ProgramMainRes programMainRes = new ProgramMainRes();
            programMainRes.setProgramName(program.getProgramName());
            programMainRes.setLike(program.getProgramLike());
            programMainRes.setPhotoUrl(program.getPhotoUrl());
            String strRemainDay = DurationCalcurator.getDuration(program.getRecruitEndDate());
            programMainRes.setRemainDay(strRemainDay);
            return programMainRes;
        }).collect(Collectors.toList());
    }
}
