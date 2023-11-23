package kusitms.gallae.config;


import kusitms.gallae.domain.Program;
import kusitms.gallae.repository.program.ProgramRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component // 추가
@EnableAsync
public class ProgramScheduler {



    private final ProgramRespository programRespository;


    @Transactional
    @Scheduled(cron = "0 5 0 * * *",zone = "Asia/Seoul")
    public void programFinishScheduler(){
        List<Program> programList = programRespository.findAll();
        //KTC로 바꾸기
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1).plusHours(9);
        LocalDate localDate = localDateTime.toLocalDate();
        programList.forEach(program -> {
            if(program.getRecruitEndDate().isBefore(localDate)) {
                program.setStatus(Program.ProgramStatus.FINISH);
                programRespository.save(program);
            }
        });

    }
}
