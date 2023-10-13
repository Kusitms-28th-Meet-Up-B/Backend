package kusitms.gallae.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtException extends RuntimeException{
    private String message;
}
