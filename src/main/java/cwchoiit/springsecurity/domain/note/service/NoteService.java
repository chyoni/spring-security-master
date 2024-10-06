package cwchoiit.springsecurity.domain.note.service;

import cwchoiit.springsecurity.domain.note.dto.NoteRegisterDTO;
import cwchoiit.springsecurity.domain.note.entity.Note;
import cwchoiit.springsecurity.domain.user.entity.User;

import java.util.List;

public interface NoteService {
    List<Note> findByUser(User user);

    Note saveNote(User user, NoteRegisterDTO noteRegisterDTO);

    void deleteNote(User user, Long noteId);
}
