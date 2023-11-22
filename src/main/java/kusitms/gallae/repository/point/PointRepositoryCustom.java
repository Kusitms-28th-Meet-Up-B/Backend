package kusitms.gallae.repository.point;

import kusitms.gallae.domain.Point;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.dto.program.ProgramSearchReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointRepositoryCustom {
    Page<Point> getDynamicPoint(User user, String type, String period, Pageable pageable);
}
