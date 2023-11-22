package kusitms.gallae.dto.archive;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArchiveEditReq {
    private Long archiveId;

    private String writer;

    private String title;

    private String category;

    private String body;

    private String hashTags;

    private String fileName;

    private String fileUrl;

    private MultipartFile file;
}
