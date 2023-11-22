package kusitms.gallae.dto.archive;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ArchiveEditModel {

    private Long archiveId;

    private String title;

    private String category;

    private String body;

    private String hashTags;

    private String writer;

    private MultipartFile file;
}
