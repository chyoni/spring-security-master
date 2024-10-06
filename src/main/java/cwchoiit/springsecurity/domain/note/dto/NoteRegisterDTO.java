package cwchoiit.springsecurity.domain.note.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoteRegisterDTO {
    private String title;
    private String content;
}
