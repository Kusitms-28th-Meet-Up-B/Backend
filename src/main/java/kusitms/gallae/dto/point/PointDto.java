package kusitms.gallae.dto.point;

import kusitms.gallae.domain.Point;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PointDto {
    private Long pointId;
    private Integer pointScore;
    private String pointActivity;
    private String pointCategory;
    private LocalDateTime date;
    private LocalTime time;

    public PointDto(Point point) {
        this.pointId = point.getPointId();
        this.pointScore = point.getPointScore();
        this.pointActivity = point.getPointActivity();
        this.pointCategory = point.getPointCategory();
        this.date = point.getDate();
        this.time = point.getTime();
    }


}
