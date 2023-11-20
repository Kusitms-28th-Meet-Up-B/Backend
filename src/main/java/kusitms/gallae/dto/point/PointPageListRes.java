package kusitms.gallae.dto.point;


import lombok.Data;

import java.util.List;

@Data
public class PointPageListRes {

    private Integer totalPage;

    private Long userPoint;

    private List<PointListRes> points;

}
