package kusitms.gallae;

import kusitms.gallae.global.Role;
import kusitms.gallae.global.jwt.JwtProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JwtTest {

    @Test
    @DisplayName("JWT 생성확인")
    void createJwt(){
        //given
        JwtProvider jwtProvider = new JwtProvider();

        //when
        String jwtToken = jwtProvider.createToken("test", List.of(Role.USER));

        //then
        Assertions.assertNotNull(jwtToken);
    }
}
