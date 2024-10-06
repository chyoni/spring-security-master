package cwchoiit.springsecurity.domain.note.repository;

import cwchoiit.springsecurity.domain.note.entity.Note;
import cwchoiit.springsecurity.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserOrderByIdDesc(User user);

    Optional<Note> findByIdAndUser(Long id, User user);
}
