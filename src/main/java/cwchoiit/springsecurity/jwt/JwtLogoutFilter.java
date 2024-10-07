package cwchoiit.springsecurity.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtLogoutFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!httpServletRequest.getRequestURI().equals("/logout")) {
            chain.doFilter(request, response);
            return;
        }

        if (!httpServletRequest.getMethod().equalsIgnoreCase("GET")) {
            chain.doFilter(request, response);
            return;
        }

        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);

        httpServletResponse.sendRedirect("/login");
    }
}
