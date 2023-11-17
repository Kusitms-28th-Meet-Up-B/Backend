package kusitms.gallae.controller;

import jakarta.servlet.http.HttpServletResponse;
import kusitms.gallae.dto.user.LoginRequestDto;
import kusitms.gallae.dto.user.LoginResponse;
import kusitms.gallae.service.user.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    // 로그인 요청을 처리하는 메서드
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestDto loginRequestDto,
                                               HttpServletResponse httpServletResponse) {
        try {
            LoginResponse loginResponse = authenticationService.login(loginRequestDto, httpServletResponse);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
