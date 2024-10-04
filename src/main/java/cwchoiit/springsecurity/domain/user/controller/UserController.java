package cwchoiit.springsecurity.domain.user.controller;

import cwchoiit.springsecurity.domain.user.dto.UserRegisterDTO;
import cwchoiit.springsecurity.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/user/signup")
    public String signup() {
        return "/signup";
    }

    @PostMapping("/user/signup")
    public String signup(@ModelAttribute UserRegisterDTO userRegisterDTO) {
        userService.signup(userRegisterDTO);
        return "redirect:/login";
    }
}
