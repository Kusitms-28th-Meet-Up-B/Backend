package kusitms.gallae.config;


import kusitms.gallae.global.Role;
import kusitms.gallae.global.jwt.JwtAccessDeniedHandler;
import kusitms.gallae.global.jwt.JwtAuthenticationEntryPoint;
import kusitms.gallae.global.jwt.JwtAuthenticationFilter;
import kusitms.gallae.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {
    private final JwtProvider jwtAuthenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers( "/swagger-resources/**",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/webjars/**");
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .authorizeHttpRequests()
                .requestMatchers("/manager/**").hasRole(Role.MANAGER.getValue())
                .requestMatchers("/reviews/saveReview").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .requestMatchers("/reviews/detail").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .requestMatchers("/reviews/editReview").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .requestMatchers("/archives/saveArchive").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .requestMatchers("/archives/detail").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .requestMatchers("/archives/editArchive").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .requestMatchers("/favorite/**").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .requestMatchers("/users/myPosts/**").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .requestMatchers("/point/list/**").hasAnyRole(Role.USER.getValue(),Role.MANAGER.getValue())
                .anyRequest().permitAll().and()
                .exceptionHandling()
                .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                .accessDeniedHandler(this.jwtAccessDeniedHandler).and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(this.jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

}
