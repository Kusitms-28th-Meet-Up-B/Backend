package kusitms.gallae.dto.program;


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

    @Schema(description = "모집 시작", example = "2021-11-03", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitStartDate;

    @Schema(description = "모집 끝", example = "2021-11-03", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitEndDate;

    private boolean userLikeCheck;

}
