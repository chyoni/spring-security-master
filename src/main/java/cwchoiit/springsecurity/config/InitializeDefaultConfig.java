package cwchoiit.springsecurity.config;

import cwchoiit.springsecurity.domain.note.dto.NoteRegisterDTO;
import cwchoiit.springsecurity.domain.note.service.NoteService;
import cwchoiit.springsecurity.domain.user.dto.UserRegisterDTO;
import cwchoiit.springsecurity.domain.user.entity.User;
import cwchoiit.springsecurity.domain.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = "!test") // test 에서는 예외
@RequiredArgsConstructor
public class InitializeDefaultConfig {

    private final UserService userService;
    private final NoteService noteService;

    @PostConstruct
    public void initializeDefaultUser() {
        User user = userService.signup(new UserRegisterDTO("user", "user"));
        noteService.saveNote(user, new NoteRegisterDTO("테스트", "테스트입니다."));
        noteService.saveNote(user, new NoteRegisterDTO("테스트2", "테스트입니다2."));
        noteService.saveNote(user, new NoteRegisterDTO("테스트3", "테스트입니다3."));
        noteService.saveNote(user, new NoteRegisterDTO("테스트4", "테스트입니다4."));
    }
}
