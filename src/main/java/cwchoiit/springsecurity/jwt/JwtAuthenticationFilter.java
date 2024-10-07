package cwchoiit.springsecurity.jwt;

import cwchoiit.springsecurity.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static cwchoiit.springsecurity.jwt.JwtProperties.*;

/**
 * JWT Authentication Filter: JWT 를 사용해서 인증하는 필터
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    /**
     * Spring Security 설정 클래스에서 지정한 로그인 URL("/login")에서 POST 요청을 받아 유저를 확인한다.
     * <br>
     * 내부동작은 다음과 같다. <br>
     * 유저를 확인할 땐 내부적으로 Spring Security 가 사용하는 {@code UserDetailsService.loadUserByUsername()} 메서드를 호출한다.
     * 그래서 실제로 해당 유저가 있어야하고 {@code UserDetailsService} 를 구현한 구현체가 {@code loadUserByUsername()} 메서드를 구현해야 한다.
     * 그게 {@code UsernamePasswordAuthenticationFilter} 에서 유저를 인증하는 방식이다. 내부적으로 {@code ProviderManager.authenticate()} 메서드를 호출하고,
     * {@code AbstractUserDetailsAuthenticationProvider.authenticate()} 메서드를 호출한다. 이 메서드 안에는 {@code retrieveUser()} 메서드가 호출되는데,
     * 그 메서드는 {@code DaoAuthenticationProvider} 의 {@code retrieveUser()} 이고 이 메서드에서 바로 {@code UserDetailsService.loadUserByUsername()} 메서드를 호출한다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = JwtUtils.createToken(user);

        Cookie cookie = new Cookie(COOKIE_NAME, token); // COOKIE_NAME: JwtProperties 파일에 있는 상수값 (gitignore)
        cookie.setMaxAge(EXPIRATION_TIME); // EXPIRATION_TIME: // JwtProperties 파일에 있는 상수값 (gitignore)
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("/home");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.sendRedirect("/login");
    }
}
