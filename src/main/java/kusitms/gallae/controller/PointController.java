package kusitms.gallae.controller;

import kusitms.gallae.domain.Point;
import kusitms.gallae.dto.point.PointDto;
import kusitms.gallae.service.point.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/points")
public class PointController {
    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PointDto>> getPointsByUserId(@PathVariable Long userId) {
        List<PointDto> points = pointService.getPointsByUserId(userId);
        return ResponseEntity.ok(points);
    }
}
