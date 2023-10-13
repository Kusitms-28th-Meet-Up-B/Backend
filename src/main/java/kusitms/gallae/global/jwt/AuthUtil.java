package kusitms.gallae.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthUtil {

    public static final String REFRESH_TOKEN_KEY = "galle";
    private static final String BEARER_PREFIX = "Bearer ";

    public static void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        CookieUtil.addCookie(response, REFRESH_TOKEN_KEY, refreshToken);
    }
    public static String getAccessToken(HttpServletRequest request) {
        return AuthUtil.getBearerToken(request);
    }

    public static String getRefreshToken(HttpServletRequest request) {
        String bearerToken = AuthUtil.getBearerToken(request);

        if (!bearerToken.equals("")) {
            return bearerToken;
        }

        return AuthUtil.getRefreshTokenCookie(request);
    }

    private static String getBearerToken(HttpServletRequest request) {
        String authHeader = StringUtils.trimToEmpty(request.getHeader(AUTHORIZATION));
        return StringUtils.trimToEmpty(StringUtils.substringAfter(authHeader, BEARER_PREFIX));
    }

    private static String getRefreshTokenCookie(HttpServletRequest request) {
        Cookie cookie = CookieUtil.getCookie(request, REFRESH_TOKEN_KEY).orElse(null);

        if (cookie != null) {
            return StringUtils.trimToEmpty(cookie.getValue());
        }

        return "";
    }
}
