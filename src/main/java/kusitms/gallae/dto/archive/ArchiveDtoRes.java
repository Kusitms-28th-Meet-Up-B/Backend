package kusitms.gallae.dto.archive;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArchiveDtoRes {
    private Long id;
    private String category;  // 게시판 명
    private String title;      // 제목
    private String writer;     // 글쓴이

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;  // 등록일

}
