package cwchoiit.springsecurity.domain.note.service.impl;

import cwchoiit.springsecurity.domain.note.dto.NoteRegisterDTO;
import cwchoiit.springsecurity.domain.note.entity.Note;
import cwchoiit.springsecurity.domain.note.exception.NoteNotFoundException;
import cwchoiit.springsecurity.domain.note.repository.NoteRepository;
import cwchoiit.springsecurity.domain.note.service.NoteService;
import cwchoiit.springsecurity.domain.user.entity.User;
import cwchoiit.springsecurity.domain.user.exception.UserNotFoundException;
import cwchoiit.springsecurity.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<Note> findByUser(User user) {
        Optional.ofNullable(user).orElseThrow(UserNotFoundException::new);
        User currentUser = userService.findByUsername(user.getUsername());

        if (currentUser.isAdmin()) {
            return noteRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
        return noteRepository.findByUserOrderByIdDesc(currentUser);
    }

    @Override
    public Note saveNote(User user, NoteRegisterDTO noteRegisterDTO) {
        Optional.ofNullable(user).orElseThrow(UserNotFoundException::new);
        User currentUser = userService.findByUsername(user.getUsername());
        return noteRepository.save(new Note(noteRegisterDTO.getTitle(), noteRegisterDTO.getContent(), currentUser));
    }

    @Override
    public void deleteNote(User user, Long noteId) {
        Optional.ofNullable(user).orElseThrow(UserNotFoundException::new);
        User currentUser = userService.findByUsername(user.getUsername());
        Note note = noteRepository.findByIdAndUser(noteId, currentUser)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + noteId));
        noteRepository.delete(note);
    }
}
