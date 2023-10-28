package kusitms.gallae.config;


import kusitms.gallae.global.jwt.JwtAccessDeniedHandler;
import kusitms.gallae.global.jwt.JwtAuthenticationEntryPoint;
import kusitms.gallae.global.jwt.JwtAuthenticationFilter;
import kusitms.gallae.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {
    private final JwtProvider jwtAuthenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
                .anyRequest().permitAll().and()
                .exceptionHandling()
                .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                .accessDeniedHandler(this.jwtAccessDeniedHandler).and()
                .addFilterBefore(new JwtAuthenticationFilter(this.jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }

}
