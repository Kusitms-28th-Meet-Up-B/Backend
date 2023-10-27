package kusitms.gallae.controller;

import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    @GetMapping("/recent")
    public ResponseEntity<BaseResponse<List<ProgramMainRes>>> findRecentProgram(){
        return ResponseEntity.ok(new BaseResponse<>(this.programService.getRecentPrograms()));
    }
}
