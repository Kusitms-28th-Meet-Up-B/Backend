package kusitms.gallae.dto.program;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kusitms.gallae.domain.User;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProgramSearchReq {
    private User user;

    private String programName;

    private String orderCriteria;

    private String Location;

    private String programType;

    private String detailType;

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

    private Pageable pageable;
}
