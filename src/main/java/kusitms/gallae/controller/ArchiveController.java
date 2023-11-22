package kusitms.gallae.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.Archive;

import kusitms.gallae.dto.archive.*;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.service.archive.ArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/archives")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private S3Service s3Service;

    @Operation(summary = "자료실 카테고리별 게시판 내용들 가져오기", description = """
            전체는 null로 보내주세요 
            """)
    @GetMapping("/category")
    public ResponseEntity<BaseResponse<ArchivePageRes>> getArchivesByCategory(
            @Parameter(description = "지원서 / 보고서")
            @RequestParam(value = "category", required = false)
            String category,

            @Parameter(description = "타이틀 검색")
            @RequestParam(value = "title", required = false) String title,

            @Parameter(description = "페이지 번호")
            @Positive(message = "must be greater than 0")
            @RequestParam(value = "page", defaultValue = "0")
            Integer pageNumber,

            @Parameter(description = "페이징 사이즈 (최대 100)")
            @Min(value = 1, message = "must be greater than or equal to 1")
            @Max(value = 100, message = "must be less than or equal to 100")
            @RequestParam(value = "size", defaultValue = "20")
            Integer pagingSize){

        PageRequest pageRequest = PageRequest.of(pageNumber, pagingSize);
        ArchivePageRes archivePageRes = archiveService.getArchivesByCategoryAndTitle(category, title, pageRequest);
        return ResponseEntity.ok(new BaseResponse<>(archivePageRes));
    }

    @Operation(summary = "편집 버튼 눌렀을 때 사용자와 일치하는지 체크 및 정보 반환", description = """
            편집 버튼 눌르면 권한이 있는지 체크해준다.
            \n
            만약 있으면 기존 정보들을 반환한다.
            \n 권한이 없으면 아래 코드 처럼 나옴 \n
            {\n
              "isSuccess": false,\n
              "code": 1002,\n
              "message": "작성자가 아닙니다."\n
            }
            """)
    @GetMapping("/checkEdit")
    public ResponseEntity<BaseResponse<ArchiveDetailRes>> getArchivesEditable(
            Principal principal,

            @Parameter(description = "자료실 아이디")
            @RequestParam(value = "archiveId", required = false)
            Long archiveId
    ){
        if(principal == null ){
            throw new BaseException(BaseResponseStatus.NOT_WRITER);
        }
        return ResponseEntity.ok(new BaseResponse<>(this.archiveService.checkArchiveEditable(archiveId, principal.getName())));
    }

    @Operation(summary = "게시물 삭제", description = """
            삭제 버튼 눌르면 권한이 있는지 체크해준다.
           
            \n 권한이 없으면 아래 코드 처럼 나옴 \n
            {\n
              "isSuccess": false,\n
              "code": 1002,\n
              "message": "작성자가 아닙니다."\n
            }
            """)
    @DeleteMapping("/delArchive")
    public ResponseEntity<BaseResponse> delArchive(
            Principal principal,

            @Parameter(description = "자료실 아이디")
            @RequestParam(value = "archiveId", required = true)
            Long archiveId
    ){
        if(principal == null ){
            throw new BaseException(BaseResponseStatus.NOT_WRITER);
        }
        archiveService.deleteArchive(archiveId, principal.getName());
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS));
    }

    @PostMapping("/saveArchive")
    public ResponseEntity<BaseResponse<Long>> saveArchive(
            Principal principal,
            @ModelAttribute
            ArchiveModel archiveModel
    ) throws IOException {
        String fileName = null;
        String fileUrl = null;
        if(archiveModel.getFile() != null && !archiveModel.getFile().isEmpty()) {
            fileName = archiveModel.getFile().getOriginalFilename();
            fileUrl = s3Service.upload(archiveModel.getFile());
        }
        ArchivePostReq archivePostReq = new ArchivePostReq();
        archivePostReq.setTitle(archiveModel.getTitle());
        archivePostReq.setCategory(archiveModel.getCategory());
        archivePostReq.setFileUrl(fileUrl);
        archivePostReq.setFileName(fileName);
        archivePostReq.setWriter(archiveModel.getWriter());
        archivePostReq.setBody(archiveModel.getBody());
        archivePostReq.setHashTags(archiveModel.getHashTags());
        return ResponseEntity.ok(new BaseResponse<>(archiveService.postArchive(archivePostReq,principal.getName())));

    }

    @Operation(summary = "자료실 편집", description = """
            저장이랑 다르게 
            archiveId도 같이 보내야합니다.
            """)
    @PostMapping("/editArchive")
    public ResponseEntity<BaseResponse<Long>> editArchive(
            @ModelAttribute
            ArchiveEditModel archiveEditModel
    ) throws IOException {
        String fileUrl = null;
        String originalFilename = null; // 원본 파일 이름을 저장할 변수

        if (archiveEditModel.getFile() != null && !archiveEditModel.getFile().isEmpty()) {
            originalFilename = archiveEditModel.getFile().getOriginalFilename(); // 원본 파일 이름 가져오기
            fileUrl = s3Service.upload(archiveEditModel.getFile());
        }

        ArchiveEditReq archiveEditReq = new ArchiveEditReq();
        archiveEditReq.setArchiveId(archiveEditModel.getArchiveId());
        archiveEditReq.setTitle(archiveEditModel.getTitle());
        archiveEditReq.setWriter(archiveEditModel.getWriter());
        archiveEditReq.setCategory(archiveEditModel.getCategory());
        archiveEditReq.setFileUrl(fileUrl);
        archiveEditReq.setFileName(originalFilename);
        archiveEditReq.setBody(archiveEditModel.getBody());
        archiveEditReq.setHashTags(archiveEditModel.getHashTags());

        return ResponseEntity.ok(new BaseResponse<>(archiveService.editArchive(archiveEditReq)));

    }

    @Operation(summary = "자료실 정보 가져오기", description = """
            포인트가 부족하면
            \n
            "isSuccess": false,\n
            "code": 1001,\n
            "message": "포인트가 부족합니다."\n
            이 반환합니다. 
            \n
            로그인한 유저는 포인트 15포인트 차감되고 포인트 컬럼 생성됩니다.\n
            *주의* 자료실 작성한 유저가 열람 할때는 포인트 차감이 되지 않습니다.\n
            이미 열랆한 자료실을 열어도 포인트 차감이 안됩니다.
       
            """)
    @GetMapping("/detail")
    public ResponseEntity<BaseResponse<ArchiveDetailRes>> getArchiveDetail(
            Principal principal,

            @Parameter(description = "자료실아이디")
            @RequestParam(value = "archiveId", required = true)
            Long archiveId
    ) {
        String username = null;
        if(principal != null) {
            username = principal.getName();
        }
        ArchiveDetailRes archiveDetail = archiveService.getArchiveById(archiveId, username);
        return ResponseEntity.ok(new BaseResponse<>(archiveDetail));
    }


    @Operation(summary = "자료실 좋아요순으로 게시판 내용들 가져오기")
    @GetMapping("/sorted/likes")
    public ResponseEntity<BaseResponse<ArchivePageRes>> getAllReviewsSortedByLikes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes"));
        Page<Archive> archivePage = archiveService.getAllArchivesSortedByLikes(pageRequest);
        List<ArchiveDtoRes> archiveDtos = archivePage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        ArchivePageRes archivePageRes = new ArchivePageRes();
        archivePageRes.setArchives(archiveDtos);
        archivePageRes.setTotalSize(archivePage.getTotalPages());
        return ResponseEntity.ok(new BaseResponse<>(archivePageRes));
    }

    // Review 엔티티를 ReviewDtoRes로 변환하는 메소드
    private ArchiveDtoRes convertToDto(Archive archive) {
        ArchiveDtoRes dto = new ArchiveDtoRes();
        dto.setId(archive.getId());
        dto.setCategory(archive.getCategory());
        dto.setTitle(archive.getTitle());
        dto.setWriter(archive.getWriter());
        dto.setCreatedDate(archive.getCreatedAt());
        return dto;

    }


}

