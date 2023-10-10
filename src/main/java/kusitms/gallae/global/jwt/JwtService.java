package kusitms.gallae.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusitms.gallae.global.jwt.jwtexception.JwtException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class JwtService {

    // Expiration Time
    public static final long MINUTE = 1000 * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;

    public static final long AT_EXP_TIME = 12 * HOUR;
    public static final long RT_EXP_TIME = 3 * MONTH;

    // Refresh Token 갱신 주기(Day)
    public static final long TOKEN_REFRESH_DAYS = 30;

    // Secret 일단 아무렇게나 생성
    public static final String JWT_SECRET = "sklsekfslkslekflededqveev";

    // Header
    public static final String AT_HEADER = "accessToken";
    public static final String RT_HEADER = "refreshToken";
    public static final String TOKEN_HEADER_PREFIX = "Bearer ";

    // Claim
    public static final String CLAIM_ID = "id";
    public static final String CLAIM_ROLE = "role";

    public String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)) {
            throw new JwtException("토큰 없음");
        }
        return authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(HMAC256(JWT_SECRET)).build();
        return verifier.verify(token);
    }

    public String createAccessToken(String email, Long id) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + AT_EXP_TIME))
                .withClaim(CLAIM_ID, id)
                .sign(HMAC256(JWT_SECRET));
    }

    public String createRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + RT_EXP_TIME))
                .sign(HMAC256(JWT_SECRET));
    }

    // Refresh Token의 남은 만료 일자(day) 계산
    public Long calculateRefreshExpiredDays(DecodedJWT decodedJWT) {
        long expTime = decodedJWT.getClaim("exp").asLong() * 1000;
        return (expTime - System.currentTimeMillis()) / DAY;
    }
}
