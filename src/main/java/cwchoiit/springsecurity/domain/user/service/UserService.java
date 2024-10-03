package cwchoiit.springsecurity.domain.user.service;

import cwchoiit.springsecurity.domain.user.dto.UserRegisterDTO;
import cwchoiit.springsecurity.domain.user.entity.User;

public interface UserService {
    void signup(UserRegisterDTO userRegisterDTO);

    User signupAdmin(UserRegisterDTO userRegisterDTO);
}
