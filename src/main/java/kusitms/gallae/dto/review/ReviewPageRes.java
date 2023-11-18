package kusitms.gallae.dto.review;


import lombok.Data;

import java.util.List;

@Data
public class ReviewPageRes {
    private Integer totalSize;
    private List<ReviewDtoRes> reviews;

}
