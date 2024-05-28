package Project.Security.Service;

import Project.Security.Entity.Films;
import Project.Security.Entity.Genre;
import Project.Security.Repository.CommentRepository;
import Project.Security.Repository.FilmRepository;
import Project.Security.Repository.GenreRepository;
import Project.Security.dto.AuthenticationResponse;
import Project.Security.dto.FilmDto;
import Project.Security.dto.GenreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


//============================Films==================================
@Service
@RequiredArgsConstructor

public class FilmService {
    private final GenreRepository genreRepository;
    private final FilmRepository filmRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    public ResponseEntity<AuthenticationResponse> createFilm(FilmDto dto, MultipartFile imageData, MultipartFile videoData) {
        try {
            if (dto == null || imageData == null || videoData == null || dto.getTitle() == null || dto.getGenres() == null) {
                return ResponseEntity.badRequest().build();
            }

            byte[] imageDataBytes = imageData.getBytes();
            byte[] videoDataBytes = videoData.getBytes();

            List<Genre> genres = genreRepository.findByNameIn(dto.getGenres());

            Films film = Films.builder()
                    .title(dto.getTitle())
                    .director(dto.getDirector())
                    .release_date(dto.getRelease_date())
                    .descrip(dto.getDescrip())
                    .imageData(imageDataBytes)
                    .videoData(videoDataBytes)
                    .genres(genres)
                    .build();

            Films savedFilm = filmRepository.save(film);

            if (savedFilm == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return ResponseEntity.ok(AuthenticationResponse.builder().build());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    public ResponseEntity<AuthenticationResponse> createGenre(GenreDto dto) {
        Optional<Genre> existingGenre = genreRepository.findByName(dto.getName());
        if (existingGenre.isPresent()) {
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder()
                    .build());
        }   

        var genre = Genre.builder().name(dto.getName()).build();
        genreRepository.save(genre);

        return ResponseEntity.ok(AuthenticationResponse.builder()
                .build());
    }

    public List<Films> getAllFilms() {
        return this.filmRepository.findAll();
    }
    public List<Genre> getAllGenre() {
        return this.genreRepository.findAll();
    }

    public Films findFilmById(Long id) {
        return this.filmRepository.findById(id).orElse(null);
    }

    public ResponseEntity<Void> deleteFilmsById(Long id) {
        if (this.filmRepository.existsById(id)) {
            commentRepository.deleteByFilmsId(id);
            this.filmRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();

        }
    }
    public ResponseEntity<String> deleteGenreById(Long id) {
        Optional<Genre> genreOpt = this.genreRepository.findById(id);
        if (genreOpt.isPresent()) {
            Genre genre = genreOpt.get();
            if (genre.getFilms().isEmpty()) {
                this.genreRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot delete genre. It is used in one or more films.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> updateFilm(Long id, FilmDto dto) {
        Optional<Films> filmOptional = filmRepository.findById(id);
        if (filmOptional.isPresent()) {
            Films existingFilm = filmOptional.get();
            if (dto.getTitle() != null && dto.getDirector() != null && dto.getRelease_date() != null) {
                existingFilm.setTitle(dto.getTitle());
                existingFilm.setDirector(dto.getDirector());
                existingFilm.setRelease_date(dto.getRelease_date());

                if (dto.getDescrip() != null) {
                    existingFilm.setDescrip(dto.getDescrip());
                }

                if (dto.getGenres() != null) {
                    List<Genre> genreList = genreRepository.findByNameIn(dto.getGenres());
                    if (!genreList.isEmpty()) {
                        existingFilm.getGenres().clear();
//                        existingFilm.setGenres(new HashSet<>(genreList));
                    }
                }

                this.filmRepository.save(existingFilm);
                return ResponseEntity.ok("Updated");
            } else {
                return ResponseEntity.badRequest().body("Film details cannot be null");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public List<FilmDto> findByGenreId(Long id) {
        return filmRepository.findByGenreId(id).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public FilmDto mapToDto(Films film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setTitle(film.getTitle());
        dto.setDirector(film.getDirector());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDescrip(film.getDescrip());
        List<String> genres = film.getGenres().stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
        dto.setGenres(genres);
        dto.setImageData(film.getImageData());
        dto.setVideoData(film.getVideoData());

        // Вывод в консоль для проверки
//        System.out.println("Film ID: " + film.getId() + ", Genres: " + genres);

        return dto;
    }


    public List<FilmDto> mapToDtoList(List<Films> films) {
        return films.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


}
