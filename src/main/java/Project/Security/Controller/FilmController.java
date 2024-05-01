package Project.Security.Controller;

import Project.Security.Entity.Films;
import Project.Security.Entity.Genre;
import Project.Security.Repository.GenreRepository;
import Project.Security.Service.FilmService;
import Project.Security.dto.AuthenticationResponse;
import Project.Security.dto.FilmDto;
import Project.Security.dto.GenreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//============================Films==================================
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;
    private final GenreRepository repository;
    @GetMapping("/genre")
    public ResponseEntity<List<Genre>> getAllGenre() {
        List<Genre> genres = this.service.getAllGenre();
        return ResponseEntity.ok(genres);
    }
    @PostMapping("/film")
    public ResponseEntity<AuthenticationResponse> createFilm(@ModelAttribute FilmDto dto,
                                                             @RequestParam("imageData") MultipartFile imageData,
                                                             @RequestParam("videoData") MultipartFile videoData) throws IOException {
        return ResponseEntity.ok(service.createFilm(dto, imageData, videoData).getBody());
    }

    @PostMapping("/genre")
    public ResponseEntity<?> createGenre(@RequestBody GenreDto dto) {
        Genre genre = repository.findByName(dto.getName()).orElse(null);
        if (genre != null){
            return ResponseEntity.badRequest().body("Genre already exists");
        }
        return ResponseEntity.ok().body(service.createGenre(dto));
    }
    @GetMapping("/film/{id}")
    public ResponseEntity<Optional<FilmDto>> findFilmById(@PathVariable Long id) {
        Optional<Films> film = this.service.findFilmById(id);
        if (film.isPresent()) {
            FilmDto fDto = service.mapToDto(film.get());
            return ResponseEntity.ok(Optional.of(fDto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/film")
    public ResponseEntity<List<FilmDto>> getAllFilms() {
        List<Films> films = this.service.getAllFilms();
        List<FilmDto> fDto = service.mapToDtoList(films);
        return ResponseEntity.ok(fDto);
    }
    @DeleteMapping("/film/{id}")
    public ResponseEntity<Void> deleteFilmsById(@PathVariable Long id) {
        this.service.deleteFilmsById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/genre/{id}")
    public ResponseEntity<Void> deleteGenreById(@PathVariable Long id) {
        this.service.deleteGenreById(id);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/film/{id}")
//    public ResponseEntity<String> updateFilm(@PathVariable Long id,
//                                             @RequestBody FilmDto dto) throws IOException {
//        String result = this.service.updateFilm(id, dto).getBody();
//        return ResponseEntity.ok(result);
//    }
}
