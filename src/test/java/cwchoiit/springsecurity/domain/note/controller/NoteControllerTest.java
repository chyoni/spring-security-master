package cwchoiit.springsecurity.domain.note.controller;

import cwchoiit.springsecurity.domain.user.entity.User;
import cwchoiit.springsecurity.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static cwchoiit.springsecurity.domain.user.entity.User.Role.ROLE_USER;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class NoteControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        userRepository.save(new User("user", "user", ROLE_USER));
    }

    @Test
    void getNotes_not_authentication() throws Exception {
        mockMvc.perform(get("/note"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(
            value = "user",
            userDetailsServiceBeanName = "userServiceImpl", // UserDetailsService 구현한 구현체의 빈 이름
            setupBefore = TestExecutionEvent.TEST_EXECUTION //언제 유저가 세팅되는지를 지정 (TEST_EXECUTION -> 이 테스트가 실행되기 바로 직전에 유저를 세팅)
    )
    void getNotes_authenticated() throws Exception {
        mockMvc.perform(get("/note"))
                .andExpect(status().isOk());
    }
}