package kusitms.gallae.service.user;


import jakarta.servlet.http.HttpServletResponse;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.user.LoginRequestDto;
import kusitms.gallae.dto.user.LoginResponse;
import kusitms.gallae.global.jwt.AuthUtil;
import kusitms.gallae.global.jwt.JwtProvider;
import kusitms.gallae.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    public LoginResponse login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getLoginId(), loginRequestDto.getLoginId())
        );

        // 사용자 정보 조회
        User user = userRepository.findByLoginId(loginRequestDto.getLoginId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // JWT 액세스 토큰 생성
        String accessToken = jwtProvider.createToken(user.getName(), List.of(user.getRole()));

        // 리프레시 토큰 생성 및 갱신
        user.renewRefreshToken();
        userRepository.save(user);

        // 리프레시 토큰을 쿠키에 저장
        AuthUtil.setRefreshTokenCookie(httpServletResponse, user.getRefreshToken());

        // 로그인 응답 생성 및 반환
        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .imageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(user.getRefreshToken())
                .build();

    }
}