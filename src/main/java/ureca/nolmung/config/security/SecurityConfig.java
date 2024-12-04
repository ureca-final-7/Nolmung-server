package ureca.nolmung.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ureca.nolmung.config.jwt.JWTUtil;
import ureca.nolmung.config.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        //config.setAllowedOrigins(Arrays.asList("*"));
                        config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "https://api.nolmung.org", "http://localhost:3000", "https://dev.nolmung.org", "https://nolmung.org"));
                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Arrays.asList("Cache-Control", "Content-Type", "X-Api-Key"));
                        config.setMaxAge(3600L);
                        return config;
                    }
            }))
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)


            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않으므로 STATELESS로 설정

            // URL별 권한 관리
            .authorizeHttpRequests(request -> request
                //.requestMatchers("/**").permitAll()
                .requestMatchers("/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/v1/oauth/**").permitAll()
                .requestMatchers("/v1/users/signup/**").permitAll()
                .requestMatchers("/v1/places/**").permitAll()
                .requestMatchers("/v1/recommend/bookmarks").permitAll()
                .requestMatchers("/ban-words/upload").permitAll()
                .requestMatchers("/v1/diary/public/**").permitAll()
                .anyRequest().authenticated()  // 그 외 URL은 인증 필요
            )

            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) // JWT 필터

            // OAuth2 로그인
            .oauth2Login(oauth2 -> {});

        return http.build();
    }
}
