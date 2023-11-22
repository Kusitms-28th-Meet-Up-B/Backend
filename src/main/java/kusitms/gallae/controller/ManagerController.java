package kusitms.gallae.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.Program;
import kusitms.gallae.dto.model.EditModel;
import kusitms.gallae.dto.model.PostModel;
import kusitms.gallae.dto.model.PostModelGet;
import kusitms.gallae.dto.program.ProgramDetailRes;
import kusitms.gallae.dto.program.ProgramManagerReq;
import kusitms.gallae.dto.program.ProgramPageMangagerRes;
import kusitms.gallae.dto.program.ProgramPostReq;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.global.jwt.AuthUtil;
import kusitms.gallae.service.admin.ManagerService;
import kusitms.gallae.service.program.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;


@RequiredArgsConstructor
@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ProgramService programService;

    private final ManagerService managerService;
    private final S3Service s3Service;


    @Operation(summary = "임시 프로그램 삭제", description = """
            프로그램 저장을 합니다.
            사진 뻬고는 다 필수 값입니다.
            아직 유저 부분이 구현이 안되어 로그인 없이 사용가능합니다.
            임시 저장 로직 -> 
            1. 임시저장이 되어 있는지 체크 
            1-1 안되어 있다면 없음 반환
            1-2 임시저장이 되어 있다면 저장된 값 반환
            2. 확인을 누르면 해당 로직 진행 (저장됨)
            3. 취소를 누르면 프로그램 내용들 삭제 API 호출 시켜줘야함-> 임시저장 데이터 지우기 위함 
            \n
            3번을 위한 API
            """)
    @DeleteMapping("/deleteTemp")
    public ResponseEntity<BaseResponse> deleteTempProgram(
            @Parameter(description = "프로그램 Id")
            @RequestParam(value = "programId", required = true)
            Long programId
    ) {
        //사용자 로그인 들어오면
        managerService.deleteTempProgram(programId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
    }

    @Operation(summary = "프로그램 저장", description = """
            프로그램 저장을 합니다.
            사진 뻬고는 다 필수 값입니다.
            
            포스트맨에서 테스트 하세요
            다른 API와 다르게 파일과 json Data를 구분해야합니다.
            프론트엔드 분은 아래 링크를 참고 해주세요
            
            https://leeggmin.tistory.com/7
            \n
            아직 유저 부분이 구현이 안되어 로그인 없이 사용가능합니다.
            임시 저장 로직 -> 
            1. 임시저장이 되어 있는지 체크 
            1-1 안되어 있다면 없음 반환
            1-2 임시저장이 되어 있다면 확인 취소 메시지 나옴
            2. 확인을 누르면 해당 로직 진행 임
            3. 취소를 누르면 프로그램 내용들 삭제 API 호출 시켜줘야함-> 임시저장 데이터 지우기 위함
            \n
            2번을 위한 API  
            """)
    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse<Long>> saveProgram(
            Principal principal,

            @ModelAttribute
            PostModel model
    ) throws IOException {
        String photoUrl = null;
        if(model.getPhoto() != null  && !model.getPhoto().isEmpty()) {
            photoUrl = s3Service.upload(model.getPhoto());
        }

        ProgramPostReq programPostReq = new ProgramPostReq();
        programPostReq.setProgramName(model.getProgramName());
        programPostReq.setPhotoUrl(photoUrl);
        programPostReq.setLocation(model.getLocation());
        programPostReq.setProgramType(model.getProgramType());
        programPostReq.setProgramDetailType(model.getProgramDetailType());
        programPostReq.setRecruitStartDate(model.getRecruitStartDate());
        programPostReq.setRecruitEndDate(model.getRecruitEndDate());
        programPostReq.setActiveStartDate(model.getActiveStartDate());
        programPostReq.setActiveEndDate(model.getActiveEndDate());
        programPostReq.setContact(model.getContact());
        programPostReq.setContactPhone(model.getContactPhone());
        programPostReq.setLink(model.getLink());
        programPostReq.setHashtag(model.getHashtag());
        programPostReq.setBody(model.getBody());

        return ResponseEntity.ok(new BaseResponse<>(this.managerService.postProgram(programPostReq, principal.getName())));
    }

    @Operation(summary = "프로그램 수정할 정보 가져오기", description = """
            프로그램 정보를 가져옵니다. 
            프로그램 수정을 할 때 해당 메서드로 정보를 가져오고
            postProgram으로 저장하시면 됩니다. 
            """)
    @GetMapping("/program")
    public ResponseEntity<BaseResponse<PostModelGet>> findProgramDetail(
            @Parameter(description = "프로그램 ID")
            @RequestParam(value = "id", required = true) Long id
    ){
        return ResponseEntity.ok(new BaseResponse<>(this.managerService.getProgramDetail(id)));
    }

    @Operation(summary = "관리자 프로그램중 진행되고 있는 정보들 가져오기", description = """
            진행하고 있는 프로그램 입니다.
            전체로 설정할경우 null로 보내주시면 됩니다.
            """)
    @GetMapping("/progressPrograms")
    public ResponseEntity<BaseResponse<ProgramPageMangagerRes>> findProgramManagerProgress(
            Principal principal,

            @Parameter(description = "프로그램 타입")
            @RequestParam(value = "programType", required = false)
            String programType,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "0")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize
    ){
        ProgramManagerReq programManagerReq = new ProgramManagerReq();
        programManagerReq.setProgramType(programType);
        programManagerReq.setStatus(Program.ProgramStatus.SAVE);
        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        programManagerReq.setPageable(pageRequest);
        return ResponseEntity.ok(new BaseResponse<>(this.managerService.getManagerPrograms(programManagerReq, principal.getName())));
    }

    @Operation(summary = "관리자 프로그램 중 마감된 정보들 가져오기", description = """
            진행하고 있는 프로그램 입니다.
            전체로 설정할경우 null로 보내주시면 됩니다.
            """)
    @GetMapping("/finishPrograms")
    public ResponseEntity<BaseResponse<ProgramPageMangagerRes>> findProgramManagerFinish(
            Principal principal,

            @Parameter(description = "프로그램 유형")
            @RequestParam(value = "programType", required = false)
            String programType,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "0")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize
    ){
        ProgramManagerReq programManagerReq = new ProgramManagerReq();
        programManagerReq.setProgramType(programType);
        programManagerReq.setStatus(Program.ProgramStatus.FINISH);
        PageRequest pageRequest = PageRequest.of(pageNumber,pagingSize);
        programManagerReq.setPageable(pageRequest);
        return ResponseEntity.ok(new BaseResponse<>(this.managerService.getManagerPrograms(programManagerReq, principal.getName())));
    }

    @Operation(summary = "프로그램 편집", description = """
            프로그램 저장을 합니다.
            사진 뻬고는 다 필수 값입니다.
            다른 API와 다르게 파일과 json Data를 구분해야합니다.
            프론트엔드 분은 아래 링크를 참고 해주세요
            그냥 편집
            https://leeggmin.tistory.com/7
            
            """)
    @PostMapping(value = "/editSave")
    public ResponseEntity<BaseResponse<Long>> tempSaveProgram(
            Principal principal,

            @ModelAttribute
            EditModel model
    ) throws IOException {
        String photoUrl = null;
        if(model.getPhoto() != null) {
            photoUrl = s3Service.upload(model.getPhoto());
        }
        ProgramPostReq programPostReq = new ProgramPostReq();
        programPostReq.setProgramId(Long.valueOf(model.getId()));
        programPostReq.setProgramName(model.getProgramName());
        programPostReq.setPhotoUrl(photoUrl);
        programPostReq.setLocation(model.getLocation());
        programPostReq.setProgramType(model.getProgramType());
        programPostReq.setProgramDetailType(model.getProgramDetailType());
        programPostReq.setRecruitStartDate(model.getRecruitStartDate());
        programPostReq.setRecruitEndDate(model.getRecruitEndDate());
        programPostReq.setActiveStartDate(model.getActiveStartDate());
        programPostReq.setActiveEndDate(model.getActiveEndDate());
        programPostReq.setContact(model.getContact());
        programPostReq.setContactPhone(model.getContactPhone());
        programPostReq.setLink(model.getLink());
        programPostReq.setHashtag(model.getHashtag());
        programPostReq.setBody(model.getBody());


        return ResponseEntity.ok(new BaseResponse<>(this.managerService.editProgram(programPostReq, principal.getName())));
    }

    @Operation(summary = "프로그램 임시저장", description = """
            프로그램 저장을 합니다.
            사진 뻬고는 다 필수 값입니다.
            다른 API와 다르게 파일과 json Data를 구분해야합니다.
            프론트엔드 분은 아래 링크를 참고 해주세요
            
            https://leeggmin.tistory.com/7
            
            """)
    @PostMapping(value = "/tempSave")
    public ResponseEntity<BaseResponse<Long>> tempSaveProgram(
            Principal principal,

            @ModelAttribute
            PostModel model
    ) throws IOException {
        String photoUrl = null;
        if(model.getPhoto() != null && !model.getPhoto().isEmpty()) {
            photoUrl = s3Service.upload(model.getPhoto());
        }
        ProgramPostReq programPostReq = new ProgramPostReq();
        programPostReq.setProgramName(model.getProgramName());
        programPostReq.setPhotoUrl(photoUrl);
        programPostReq.setLocation(model.getLocation());
        programPostReq.setProgramType(model.getProgramType());
        programPostReq.setProgramDetailType(model.getProgramDetailType());
        programPostReq.setRecruitStartDate(model.getRecruitStartDate());
        programPostReq.setRecruitEndDate(model.getRecruitEndDate());
        programPostReq.setActiveStartDate(model.getActiveStartDate());
        programPostReq.setActiveEndDate(model.getActiveEndDate());
        programPostReq.setContact(model.getContact());
        programPostReq.setContactPhone(model.getContactPhone());
        programPostReq.setLink(model.getLink());
        programPostReq.setHashtag(model.getHashtag());
        programPostReq.setBody(model.getBody());


        return ResponseEntity.ok(new BaseResponse<>(this.managerService.postTempProgram(programPostReq, principal.getName())));
    }


    @Operation(summary = "임시 프로그램 가져오기 ", description = """
            프로그램 저장을 합니다.
            아직 유저 부분이 구현이 안되어 로그인 없이 사용가능합니다.
            임시 저장 로직 -> 
            1. 임시저장이 되어 있는지 체크 
            1-1 안되어 있다면 없음 반환
            1-2 임시저장이 되어 있다면 저장된 값 반환
            2. 확인을 누르면 해당 로직 진행 (저장됨)
            3. 취소를 누르면 프로그램 내용들 삭제 API 호출 시켜줘야함-> 임시저장 데이터 지우기 위함 
            \n
            1번을 위한 API
            """)
    @GetMapping("/findTemp")
    public ResponseEntity<BaseResponse<ProgramPostReq>> findTempProgram(Principal principal) {

        //사용자 로그인 들어오면
        return ResponseEntity.ok(new BaseResponse<>(this.managerService.getTempProgram(principal.getName())));
    }



    @Operation(summary = "프로그램 마감", description = """
            프로그램을 마감하는 버튼 클릭시
            프로그램 상태가 마감으로 변경됨
            """)
    @PostMapping("/finish")
    public ResponseEntity<BaseResponse> finishProgram(
            @Parameter(description = "프로그램 ID")
            @RequestParam(value = "id", required = true) Long programId
    ) {
        managerService.finishProgram(programId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
    }

    @Operation(summary = "프로그램 삭제", description = """
            프로그램을 삭제합니다.
            """)
    @DeleteMapping("/deleteProgram")
    public ResponseEntity<BaseResponse> deleteProgram(
            @Parameter(description = "프로그램 Id")
            @RequestParam(value = "programId", required = true)
            Long programId
    ) {
        //사용자 로그인 들어오면
        managerService.deleteProgram(programId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
    }
}
