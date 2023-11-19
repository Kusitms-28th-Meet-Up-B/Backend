package kusitms.gallae.repository.favorite;

import kusitms.gallae.domain.Program;
import kusitms.gallae.dto.favorite.FavoriteSearchReq;

import java.util.List;

public interface FavoriteRepositoryCustom {

    List<Program> getFavoritePrograms(FavoriteSearchReq favoriteSearchReq);
}
