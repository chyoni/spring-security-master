package cwchoiit.springsecurity.config;

import cwchoiit.springsecurity.domain.user.repository.UserRepository;
import cwchoiit.springsecurity.filter.StopWatchFilter;
import cwchoiit.springsecurity.jwt.JwtAuthenticationFilter;
import cwchoiit.springsecurity.jwt.JwtAuthorizationFilter;
import cwchoiit.springsecurity.jwt.JwtLogoutFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthenticationConfiguration configuration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new StopWatchFilter(), WebAsyncManagerIntegrationFilter.class)
                .addFilterAt(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class) // 기존 Username, Password 를 Form 으로 받아 인증하는 방식의 스프링 시큐리티의 필터를 JwtAuthenticationFilter 로 변경
                .addFilterAt(new JwtAuthorizationFilter(userRepository), AuthorizationFilter.class) // 기존의 인가를 처리하는 스프링 시큐리티의 필터를 JWT 로 인가하는 방식의 JwtAuthorizationFilter 로 변경
                .addFilterAt(new JwtLogoutFilter(), LogoutFilter.class) // 기존의 로그아웃 처리를 하는 스프링 시큐리티의 필터를 직접 만든 JwtLogoutFilter 로 변경
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic authentication disabled
                .csrf(AbstractHttpConfigurer::disable) // CSRF protection disabled
                .rememberMe(AbstractHttpConfigurer::disable) // RememberMe authentication disabled
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // session authentication disabled
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/home", "/h2-console/**", "/css/**", "/js/**", "/images/**", "/login", "/user/signup").permitAll()
                        .requestMatchers("/note").hasRole("USER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // H2-Console iframe 정상 작동 (Origin 비교 후 같으면 그 iframe 요청은 허용)
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login"));
        return http.build();
    }
}
