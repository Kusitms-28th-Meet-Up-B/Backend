package kusitms.gallae.dto.archive;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArchiveModel {
    private String title;

    private String category;

    private String body;

    private String hashTags;

    private MultipartFile file;
    private String writer;

}
