package Project.Security.Controller;

import Project.Security.Entity.Comment;
import Project.Security.Service.CommentService;
import Project.Security.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
//18
    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto commentDto) {
        Comment newComment = commentService.addComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }
//    19
    @GetMapping("/comments/{filmId}")
    public ResponseEntity<List<Comment>> getAllCommentsForFilm(@PathVariable Long filmId) {
        List<Comment> comments = commentService.getAllCommentsForFilm(filmId);
        return ResponseEntity.ok(comments);
    }
//    20
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        return commentService.deleteCommentById(id);
    }


}