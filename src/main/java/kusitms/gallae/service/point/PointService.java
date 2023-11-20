package kusitms.gallae.service.point;


import jakarta.transaction.Transactional;
import kusitms.gallae.domain.Point;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.point.PointListRes;
import kusitms.gallae.dto.point.PointPageListRes;
import kusitms.gallae.dto.program.ProgramMainRes;
import kusitms.gallae.dto.program.ProgramPageMainRes;
import kusitms.gallae.repository.point.PointRepositoryCustom;
import kusitms.gallae.repository.point.PointRepositoryImpl;
import kusitms.gallae.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepositoryCustom pointRepositoryCustom;

    public PointPageListRes getPointList(String type, String period, String username, Pageable pageable) {
        User user = userRepository.findById(Long.valueOf(username)).orElse(null);
        Page<Point> points = pointRepositoryCustom.getDynamicPoint(user,type,period,pageable);
        List<Point> pageToListNewPrograms = points.getContent();
        List<PointListRes> pointListResList = pageToListNewPrograms.stream().map(point -> {
            PointListRes pointListRes = new PointListRes();
            pointListRes.setId(point.getPointId());
            pointListRes.setType(point.getPointCategory());
            pointListRes.setActivityDetails(point.getPointActivity());
            pointListRes.setDate(point.getDate());
            pointListRes.setTime(point.getTime());
            pointListRes.setPointScore(point.getPointScore());
            return pointListRes;
        }).collect(Collectors.toList());
        PointPageListRes pointPageListRes = new PointPageListRes();
        pointPageListRes.setPoints(pointListResList);
        pointPageListRes.setTotalPage(points.getTotalPages());
        return pointPageListRes;
    }
}
