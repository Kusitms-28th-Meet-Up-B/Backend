package kusitms.gallae.dto.favorite;

import kusitms.gallae.domain.User;
import lombok.Data;

@Data
public class FavoriteSearchReq {
    private User user;
    private String location;
    private String programType;
    private String status;
}
