package kusitms.gallae.service.point;

import kusitms.gallae.domain.Point;
import kusitms.gallae.dto.point.PointDto;
import kusitms.gallae.repository.point.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointService {
    private final PointRepository pointRepository;

    @Autowired
    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public List<PointDto> getPointsByUserId(Long userId) {
        List<Point> points = pointRepository.findByUserId(userId);
        return points.stream().map(PointDto::new).collect(Collectors.toList());
    }

}
