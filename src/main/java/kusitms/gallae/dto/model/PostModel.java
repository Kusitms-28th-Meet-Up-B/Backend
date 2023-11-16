package kusitms.gallae.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Data
@ToString
public class PostModel {

    private String programName;

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
