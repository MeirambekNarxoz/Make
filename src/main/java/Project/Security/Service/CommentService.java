package Project.Security.Service;

import Project.Security.Entity.Comment;
import Project.Security.Entity.Films;
import Project.Security.Entity.User;
import Project.Security.Repository.CommentRepository;
import Project.Security.Repository.FilmRepository;
import Project.Security.Repository.UserRepository;
import Project.Security.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final CommentRepository commentRepository;

    public Comment addComment(CommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + commentDto.getUserId()));
        Films film = filmRepository.findById(commentDto.getFilmId()).orElseThrow(() -> new IllegalArgumentException("Film not found with id: " + commentDto.getFilmId()));
        Comment newComment = new Comment();
        newComment.setUser(user);
        newComment.setFilms(film);
        newComment.setCommentary(commentDto.getCommentary());
        return commentRepository.save(newComment);
    }
    public List<Comment> getAllCommentsForFilm(Long filmId) {
        return commentRepository.findByFilmsId(filmId);
    }
    public ResponseEntity<Void> deleteCommentById(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
