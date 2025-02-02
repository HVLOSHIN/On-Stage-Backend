package com.team5.on_stage.global.config;

import com.team5.on_stage.global.config.auth.CustomClientRegistrationRepo;
import com.team5.on_stage.global.config.auth.CustomOAuth2AuthorizedClientService;
import com.team5.on_stage.global.config.auth.CustomOAuth2UserService;
import com.team5.on_stage.global.config.auth.OAuth2SuccessHandler;
import com.team5.on_stage.global.config.auth.refresh.RefreshService;
import com.team5.on_stage.global.config.jwt.CustomLogoutFilter;
import com.team5.on_stage.global.config.jwt.JwtFilter;
import com.team5.on_stage.global.config.jwt.JwtUtil;
import com.team5.on_stage.global.exception.JwtAccessDeniedHandler;
import com.team5.on_stage.global.exception.JwtAuthenticationEntryPointHandler;
import com.team5.on_stage.global.config.auth.refresh.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomClientRegistrationRepo customClientRegistrationRepo;
    private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final RefreshService refreshService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors((cors) -> cors.configurationSource(new CorsConfig()));

        http
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.logoutUrl("/logout"));

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/login/**", "/api/auth/reissue").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() /* Swagger */
                .requestMatchers("/usertest").authenticated()
                .anyRequest().permitAll());

        http
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPointHandler(jwtUtil))
                        .accessDeniedHandler(new JwtAccessDeniedHandler()))
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository, refreshService), LogoutFilter.class);

        http.oauth2Login((oauth2) -> oauth2
                //.loginPage("/login")
                .authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepo.ClientRegistrationRepository()))
                .clientRegistrationRepository(customClientRegistrationRepo.ClientRegistrationRepository())
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(customOAuth2UserService))));


        return http.build();
    }
}
