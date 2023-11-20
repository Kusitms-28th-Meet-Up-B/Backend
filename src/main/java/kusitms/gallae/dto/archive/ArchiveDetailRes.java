package kusitms.gallae.dto.archive;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArchiveDetailRes {

    private Long id;
    private String category;
    private String title;
    private String writer;
    private String fileName;

    private String fileUrl;

    private String hashtag;
    private String body;
    private Long previousId;
    private Long nextId;

    private boolean likeCheck;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;  // 등록일

}
