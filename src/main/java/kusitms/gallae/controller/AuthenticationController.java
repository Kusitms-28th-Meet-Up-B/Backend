package kusitms.gallae.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusitms.gallae.config.BaseResponse;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.dto.user.LoginRequestDto;
import kusitms.gallae.dto.user.LoginResponse;
import kusitms.gallae.dto.user.RenewTokenResponse;
import kusitms.gallae.global.jwt.AuthUtil;
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
            return ResponseEntity.ok(new BaseResponse<>(authenticationService.login(loginRequestDto, httpServletResponse)));
        } catch (RuntimeException e) {

            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.NOT_FOUND));
        }
    }

    @Operation(
            summary = "토큰 갱신",
            description = """
                    리프레시 토큰을 통해, 엑세스 토큰과 리프레시 토큰을 재발급받는 API

                    현재 보유 중인 엑세스 토큰이 만료된 경우에 호출 (엑세스 토큰을 요구하는 API에서 code 401이 발생하면 만료된 것)

                    갱신 후 기존 엑세스 토큰과 리프레시 토큰은 더이상 사용할 수 없음"""
    )
    @GetMapping("/refresh")
    public ResponseEntity<BaseResponse<RenewTokenResponse>> renewAccessToken(
            @Parameter(description = "refreshToken")
            @RequestParam(value = "refreshToken", required = false)
            String refreshToken,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        RenewTokenResponse response = this.authenticationService.renewToken(refreshToken);

        AuthUtil.setRefreshTokenCookie(httpServletResponse, response.getRefreshToken());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}
