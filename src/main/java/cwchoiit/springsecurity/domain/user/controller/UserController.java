package cwchoiit.springsecurity.domain.user.controller;

import cwchoiit.springsecurity.domain.user.dto.UserRegisterDTO;
import cwchoiit.springsecurity.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String signup() {
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute UserRegisterDTO userRegisterDTO) {
        userService.signup(userRegisterDTO);
        return "redirect:/login";
    }
}
