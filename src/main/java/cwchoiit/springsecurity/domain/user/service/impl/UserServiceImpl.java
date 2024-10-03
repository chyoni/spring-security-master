package cwchoiit.springsecurity.domain.user.service.impl;

import cwchoiit.springsecurity.domain.user.dto.UserRegisterDTO;
import cwchoiit.springsecurity.domain.user.entity.User;
import cwchoiit.springsecurity.domain.user.exception.AlreadyRegisterUserException;
import cwchoiit.springsecurity.domain.user.repository.UserRepository;
import cwchoiit.springsecurity.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()) {
            throw new AlreadyRegisterUserException("User already exists");
        }
        User newUser = new User(userRegisterDTO.getUsername(), passwordEncoder.encode(userRegisterDTO.getPassword()), "ROLE_USER");
        userRepository.save(newUser);
    }

    @Override
    public User signupAdmin(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()) {
            throw new AlreadyRegisterUserException("User already exists");
        }
        User newAdmin = new User(userRegisterDTO.getUsername(), passwordEncoder.encode(userRegisterDTO.getPassword()), "ROLE_ADMIN");
        return userRepository.save(newAdmin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username).orElse(null);
        if (findUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(findUser.getUsername(), findUser.getPassword(), findUser.getAuthorities());
    }
}
