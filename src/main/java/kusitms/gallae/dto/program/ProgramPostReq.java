package kusitms.gallae.dto.program;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ProgramPostReq {

    private String programName;

    private String photoUrl;

    private String location;

    private String programType;

    private String programDetailType;

    @Schema(description = "모집 시작", example = "2021-11-03", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitStartDate;

    @Schema(description = "모집 끝", example = "2021-11-03", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitEndDate;

    @Schema(description = "활동 시작", example = "2021-11-03", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate activeStartDate;

    @Schema(description = "활동 ", example = "2021-11-03", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate activeEndDate;

    private String contact;

    private String contactPhone;

    private String link;

    private String hashtag;

    private String body;
}