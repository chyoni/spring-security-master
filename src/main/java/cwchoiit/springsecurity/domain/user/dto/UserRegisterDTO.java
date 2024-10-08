package cwchoiit.springsecurity.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class UserRegisterDTO {

    private String username;
    private String password;
}
