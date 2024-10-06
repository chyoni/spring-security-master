package cwchoiit.springsecurity.domain.user.service.impl;

import cwchoiit.springsecurity.domain.user.dto.UserRegisterDTO;
import cwchoiit.springsecurity.domain.user.entity.User;
import cwchoiit.springsecurity.domain.user.exception.AlreadyRegisterUserException;
import cwchoiit.springsecurity.domain.user.exception.UserNotFoundException;
import cwchoiit.springsecurity.domain.user.repository.UserRepository;
import cwchoiit.springsecurity.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cwchoiit.springsecurity.domain.user.entity.User.Role.ROLE_ADMIN;
import static cwchoiit.springsecurity.domain.user.entity.User.Role.ROLE_USER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User signup(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()) {
            throw new AlreadyRegisterUserException("User already exists");
        }
        User newUser = new User(userRegisterDTO.getUsername(), passwordEncoder.encode(userRegisterDTO.getPassword()), ROLE_USER);
        return userRepository.save(newUser);
    }

    @Override
    public User signupAdmin(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()) {
            throw new AlreadyRegisterUserException("User already exists");
        }
        User newAdmin = new User(userRegisterDTO.getUsername(), passwordEncoder.encode(userRegisterDTO.getPassword()), ROLE_ADMIN);
        return userRepository.save(newAdmin);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username).orElse(null);
        if (findUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(findUser.getUsername(), findUser.getPassword(), findUser.getRole());
    }
}
