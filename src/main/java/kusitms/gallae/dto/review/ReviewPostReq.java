package kusitms.gallae.dto.review;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewPostReq {
    private String writer;

    private String title;

    private String category;

    private String body;

    private String hashTags;

    private String fileName;

    private String fileUrl;
}
