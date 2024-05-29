package Project.Security.Controller;

import Project.Security.Entity.Comment;
import Project.Security.Entity.Films;
import Project.Security.Entity.Genre;
import Project.Security.Service.FilmFacade;
import Project.Security.dto.AuthenticationResponse;
import Project.Security.dto.CommentDto;
import Project.Security.dto.FilmDto;
import Project.Security.dto.GenreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FilmController {
    private final FilmFacade filmFacade;

    @GetMapping("/genre")
    public ResponseEntity<List<Genre>> getAllGenre() {
        return ResponseEntity.ok(filmFacade.getAllGenre());
    }

    @PostMapping(value = "/film", consumes = "multipart/form-data")
    public ResponseEntity<AuthenticationResponse> createFilm(@RequestPart("film") FilmDto dto,
                                                             @RequestPart("imageData") MultipartFile imageData,
                                                             @RequestPart("videoData") MultipartFile videoData) throws IOException {
        return ResponseEntity.ok(filmFacade.createFilm(dto, imageData, videoData).getBody());
    }

    @PostMapping("/genre")
    public ResponseEntity<?> createGenre(@RequestBody GenreDto dto) {
        return ResponseEntity.ok().body(filmFacade.createGenre(dto));
    }

    @GetMapping("/film/{id}")
    public ResponseEntity<?> findFilmById(@PathVariable Long id) {
        Films film = filmFacade.findFilmById(id);
        if (film != null) {
            return ResponseEntity.ok(filmFacade.mapToDto(film));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/film")
    public ResponseEntity<List<FilmDto>> getAllFilms() {
        return ResponseEntity.ok(filmFacade.mapToDtoList(filmFacade.getAllFilms()));
    }

    @DeleteMapping("/film/{id}")
    public ResponseEntity<Void> deleteFilmsById(@PathVariable Long id) {
        return filmFacade.deleteFilmsById(id);
    }

    @DeleteMapping("/genre/{id}")
    public ResponseEntity<String> deleteGenreById(@PathVariable Long id) {
        return filmFacade.deleteGenreById(id);
    }

    @PutMapping("/film/{id}")
    public ResponseEntity<String> updateFilm(@PathVariable Long id, @RequestBody FilmDto dto) throws IOException {
        return ResponseEntity.ok(filmFacade.updateFilm(id, dto).getBody());
    }

    @GetMapping("/filmGenre/{id}")
    public List<FilmDto> getByGenreId(@PathVariable("id") Long id) {
        return filmFacade.findByGenreId(id);
    }

    @GetMapping("/filmTest/{id}")
    public Films getById(@PathVariable("id") Long id) {
        return filmFacade.findFilmById(id);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto commentDto) {
        Comment newComment = filmFacade.addComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    @GetMapping("/comments/{filmId}")
    public ResponseEntity<List<Comment>> getAllCommentsForFilm(@PathVariable Long filmId) {
        List<Comment> comments = filmFacade.getAllCommentsForFilm(filmId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        return filmFacade.deleteCommentById(id);
    }
}
