package kusitms.gallae.dto.program;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProgramMainRes {

    private Long id;

    private String photoUrl;

    private String programName;

    private Float latitude;

    private Float longitude;

    private Long Like;

    private String remainDay;

    private List<String> hashTag;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate recruitStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate recruitEndDate;

    private boolean userLikeCheck;

}
