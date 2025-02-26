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
 * Spring Security 에서 UsernamePasswordAuthenticationFilter 대신 사용될 JWT 를 이용한 인증 필터
 * <p>
 * 동작 방식은 다음 순서로 진행된다. <br>
 * 1 - 이 필터를 Security Configuration 에 UsernamePasswordAuthenticationFilter 위치에 대신 등록한다. {@code addFilterAt(...)} <br>
 * 2 - UsernamePasswordAuthenticationFilter 는 따로 사용자가 지정하지 않으면 기본으로 바라보고 있는 로그인 URL = POST["/login"] <br>
 * 3 - 해당 경로로 인증 정보를 추가해서 요청하면 가장 먼저 이 필터의 {@code attemptAuthentication}가 실행된다. <br>
 * 4 - 여기서, 사용자가 입력한 인증 정보를 바디 데이터로부터 읽어서 인증 시도를 한다. <br>
 * 5 - 내부적으로 {@code ProviderManager.authenticate()} 메서드를 호출하고,
 * {@code AbstractUserDetailsAuthenticationProvider.authenticate()} 메서드를 호출한다. 이 메서드 안에는 {@code retrieveUser()} 메서드가 호출되는데,
 * 그 메서드는 {@code DaoAuthenticationProvider}의 {@code retrieveUser()}이고 이 메서드에서 바로 {@code UserDetailsService.loadUserByUsername()}메서드를 호출한다. <br>
 * 6 - {@code loadUserByUsername()}을 호출하면 내가 {@link cwchoiit.springsecurity.domain.user.service.impl.UserServiceImpl} 에서 재정의한 메서드가 호출된다. <br>
 * 7 - {@code loadUserByUsername()}가 정상적으로 {@link org.springframework.security.core.userdetails.User}를 반환하면
 * 이 필터의 {@code successfulAuthentication}를 호출한다. 여기서 로그인 성공 처리 로직을 작성하면 된다. <br>
 * 8 - {@code loadUserByUsername()}가 정상적으로 반환되지 않으면 이 필터의 {@code unsuccessfulAuthentication}을 호출한다. <br>
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // setFilterProcessesUrl(...)로 로그인 처리 URL 따로 지정하지 않으면, 기본값은 POST, "/login" (UsernamePasswordAuthenticationFilter 내부에 지정)
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

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
