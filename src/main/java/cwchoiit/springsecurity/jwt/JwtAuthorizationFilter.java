package cwchoiit.springsecurity.jwt;


import cwchoiit.springsecurity.domain.user.entity.User;
import cwchoiit.springsecurity.domain.user.exception.UserNotFoundException;
import cwchoiit.springsecurity.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * JWT Authorization Filter: JWT 를 사용해서 인가하는 필터
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (token != null) {
            try {
                Authentication authentication = getUsernamePasswordAuthenticationToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthenticationToken(String token) {
        String username = JwtUtils.getUsername(token);
        if (username != null) {
            User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        return null;
    }
}
