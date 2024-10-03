package cwchoiit.springsecurity.config;

import cwchoiit.springsecurity.domain.user.entity.User;
import cwchoiit.springsecurity.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable); // HTTP Basic authentication disabled
        http.csrf(AbstractHttpConfigurer::disable); // CSRF protection disabled
        http.rememberMe(configurer -> configurer.key("rememberMe").tokenValiditySeconds(86400)); // 1 day

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers("/", "/home", "/signup", "/css/**", "/js/**", "/images/**").permitAll()
                    .requestMatchers("/note").hasRole("USER")
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                    .anyRequest().authenticated());

        http.formLogin(formLogin -> formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll());

        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/signup"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }
}
