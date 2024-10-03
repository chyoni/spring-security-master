package cwchoiit.springsecurity.domain.user.service.impl;

import cwchoiit.springsecurity.domain.user.dto.UserRegisterDTO;
import cwchoiit.springsecurity.domain.user.entity.User;
import cwchoiit.springsecurity.domain.user.exception.AlreadyRegisterUserException;
import cwchoiit.springsecurity.domain.user.repository.UserRepository;
import cwchoiit.springsecurity.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(UserRegisterDTO userRegisterDTO) {
        userRepository.findByUsername(userRegisterDTO.getUsername()).orElseThrow(AlreadyRegisterUserException::new);
        User newUser = new User(userRegisterDTO.getUsername(), passwordEncoder.encode(userRegisterDTO.getPassword()), "ROLE_USER");
        userRepository.save(newUser);
    }

    @Override
    public User signupAdmin(UserRegisterDTO userRegisterDTO) {
        userRepository.findByUsername(userRegisterDTO.getUsername()).orElseThrow(AlreadyRegisterUserException::new);
        User newAdmin = new User(userRegisterDTO.getUsername(), passwordEncoder.encode(userRegisterDTO.getPassword()), "ROLE_ADMIN");
        return userRepository.save(newAdmin);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
