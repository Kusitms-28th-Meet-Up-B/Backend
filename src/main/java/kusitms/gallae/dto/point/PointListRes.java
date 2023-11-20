package kusitms.gallae.dto.point;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PointListRes {


    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime time;

    private String type;

    private String activityDetails;

    private Integer pointScore;
}
