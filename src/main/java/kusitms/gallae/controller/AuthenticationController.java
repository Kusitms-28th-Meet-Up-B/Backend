package kusitms.gallae.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.dto.user.LoginRequestDto;
import kusitms.gallae.dto.user.LoginResponse;
import kusitms.gallae.service.user.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // 로그인 요청을 처리하는 메서드
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(
            @Parameter(description = "아이디", example = "아이")
            @RequestParam(value = "loginId", required = true)
            String loginId,

            @Parameter(description = "패스워드")
            @RequestParam(value = "loginPassword", required = true)
            String loginPw,

            HttpServletResponse httpServletResponse) {
        try {

            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setLoginId(loginId);
            loginRequestDto.setLoginPw(loginPw);
            System.out.println(loginRequestDto.getLoginPw());
            return ResponseEntity.ok(new BaseResponse<>(authenticationService.login(loginRequestDto, httpServletResponse)));
        } catch (RuntimeException e) {

            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.NOT_FOUND));
        }
    }
}
