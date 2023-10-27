package kusitms.gallae;

import kusitms.gallae.global.Role;
import kusitms.gallae.global.jwt.JwtProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Test
    void DurationTest(){
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime dateTime = LocalDate.of(2023, 10 ,31).atStartOfDay();
        Assertions.assertEquals(Duration.between(today,dateTime).toDays(),4);
    }
}
