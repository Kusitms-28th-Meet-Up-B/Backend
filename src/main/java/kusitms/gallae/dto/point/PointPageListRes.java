package kusitms.gallae.dto.point;


import lombok.Data;

import java.util.List;

@Data
public class PointPageListRes {

    private Integer totalPage;

    private List<PointListRes> points;

}
