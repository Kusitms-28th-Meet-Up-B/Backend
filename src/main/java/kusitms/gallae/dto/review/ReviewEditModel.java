package kusitms.gallae.dto.review;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ReviewEditModel {

    private Long reviewId;

    private String title;

    private String category;

    private String body;

    private String hashTags;

    private String writer;

    private MultipartFile file;

}
