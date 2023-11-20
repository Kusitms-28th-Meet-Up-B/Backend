package kusitms.gallae.dto.review;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewModel {
    private String title;

    private String category;

    private String body;

    private String hashTags;

    private MultipartFile file;



}
