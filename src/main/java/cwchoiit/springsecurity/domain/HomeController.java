package cwchoiit.springsecurity.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        log.info("GET /home");
        SecurityContext context = SecurityContextHolder.getContext();

        Authentication authentication = context.getAuthentication();

        authentication.getPrincipal();
        authentication.getAuthorities();
        authentication.getCredentials();
        authentication.getDetails();
        authentication.isAuthenticated();

        return "/home";
    }
}
