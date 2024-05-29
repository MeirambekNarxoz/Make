package Project.Security.Service;

import Project.Security.Entity.Comment;
import Project.Security.Entity.Films;
import Project.Security.Entity.Genre;
import Project.Security.dto.AuthenticationResponse;
import Project.Security.dto.CommentDto;
import Project.Security.dto.FilmDto;
import Project.Security.dto.GenreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmFacade {
    private final FilmService filmService;
    private final CommentService commentService;

    // Film-related methods
    public ResponseEntity<AuthenticationResponse> createFilm(FilmDto dto, MultipartFile imageData, MultipartFile videoData) throws IOException {
        return filmService.createFilm(dto, imageData, videoData);
    }

    public ResponseEntity<AuthenticationResponse> createGenre(GenreDto dto) {
        return filmService.createGenre(dto);
    }

    public List<Films> getAllFilms() {
        return filmService.getAllFilms();
    }

    public List<Genre> getAllGenre() {
        return filmService.getAllGenre();
    }

    public Films findFilmById(Long id) {
        return filmService.findFilmById(id);
    }

    public ResponseEntity<Void> deleteFilmsById(Long id) {
        return filmService.deleteFilmsById(id);
    }

    public ResponseEntity<String> deleteGenreById(Long id) {
        return filmService.deleteGenreById(id);
    }

    public ResponseEntity<String> updateFilm(Long id, FilmDto dto) throws IOException {
        return filmService.updateFilm(id, dto);
    }

    public List<FilmDto> findByGenreId(Long id) {
        return filmService.findByGenreId(id);
    }

    public FilmDto mapToDto(Films film) {
        return filmService.mapToDto(film);
    }

    public List<FilmDto> mapToDtoList(List<Films> films) {
        return filmService.mapToDtoList(films);
    }

    // Comment-related methods
    public Comment addComment(CommentDto commentDto) {
        return commentService.addComment(commentDto);
    }

    public List<Comment> getAllCommentsForFilm(Long filmId) {
        return commentService.getAllCommentsForFilm(filmId);
    }

    public ResponseEntity<Void> deleteCommentById(Long id) {
        return commentService.deleteCommentById(id);
    }
}
