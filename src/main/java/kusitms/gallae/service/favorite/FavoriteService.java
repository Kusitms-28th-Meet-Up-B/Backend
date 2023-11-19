package kusitms.gallae.service.favorite;

import kusitms.gallae.dto.favorite.FavoriteSearchReq;
import kusitms.gallae.dto.program.ProgramMainRes;

import java.util.List;

public interface FavoriteService {

    void postFavorite(String username,Long programId);

    List<ProgramMainRes> getFavoriteByUser(String username, FavoriteSearchReq favoriteSearchReq);
}
