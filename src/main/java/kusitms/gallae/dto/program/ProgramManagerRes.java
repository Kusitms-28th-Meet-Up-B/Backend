package kusitms.gallae.dto.program;


import lombok.Data;

import java.time.LocalDate;

@Data
public class ProgramManagerRes {
    private Long id;
    private String title;
    private Long viewCount;
    private Long like;
    private LocalDate recruitStartDate;
    private LocalDate recuritEndDate;
}
