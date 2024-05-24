package Project.Security.Repository;

import Project.Security.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFilmsId(Long filmId);
    boolean existsById(Long userId);
}
