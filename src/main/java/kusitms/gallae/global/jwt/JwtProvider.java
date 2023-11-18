package kusitms.gallae.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import kusitms.gallae.global.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class JwtProvider{

    private static final String AUTHORITIES_KEY = "authorities";

    private final String secret="wewef23fewfwfwfeefkalefalekfalekfaef2iweifejslqeqwew";
    private final long durationSeconds= 12*60*60*60*1000;


    public String createToken(String username, List<Role> roles) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(username);

        claims.put(AUTHORITIES_KEY, roles.stream().map(Role::toAuthority).collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.durationSeconds * 1000))
                .signWith(SignatureAlgorithm.HS256 , this.secret)
                .compact();
    }
    public String getUsernameFromToken(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    public Optional<Authentication> getAuthentication(String token) {
        if (!this.validateToken(token)) {
            throw new JwtException("토큰 재발급 받으세요");
        }

        Claims claims = this.getClaims(token);

        if (StringUtils.isBlank(claims.getSubject())) {
            return Optional.empty();
        }

        Collection<? extends GrantedAuthority> authorities = this.getAuthorities(claims);
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

        return Optional.of(new UsernamePasswordAuthenticationToken(userDetails, "", authorities));
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = this.getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        Object authorities = claims.get(AUTHORITIES_KEY);

        if (authorities instanceof Collection<?> authorityCollection) {
            return authorityCollection.stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
