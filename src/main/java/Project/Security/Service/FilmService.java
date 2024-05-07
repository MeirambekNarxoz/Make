package Project.Security.Service;

import Project.Security.Entity.Films;
import Project.Security.Entity.Genre;
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
    private final JwtService jwtService;
    public ResponseEntity<AuthenticationResponse> createFilm(FilmDto dto, MultipartFile imageData, MultipartFile videoData) {
        try {
            if (dto == null || imageData == null || videoData == null || dto.getTitle() == null || dto.getGenres() == null) {
                return ResponseEntity.badRequest().build();
            }

            byte[] imageDataBytes = imageData.getBytes();
            byte[] videoDataBytes = videoData.getBytes();

            Set<Genre> genres = genreRepository.findByNameIn(dto.getGenres());

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

            String jwtToken = jwtService.generateTokenFilm(savedFilm);

            return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).build());
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

        // создаем и возвращаем JWT-токен
        var jwtToken = jwtService.generateTokenGenre(genre);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .token(jwtToken)
                .build());
    }

    public List<Films> getAllFilms() {
        return this.filmRepository.findAll();
    }
    public List<Genre> getAllGenre() {
        return this.genreRepository.findAll();
    }

    public Optional<Films> findFilmById(Long id) {
        return this.filmRepository.findById(id);
    }

    public ResponseEntity<Void> deleteFilmsById(Long id) {
        if (this.filmRepository.existsById(id)) {
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

//    public ResponseEntity<String> updateFilm(Long id, FilmDto dto) {
//        Optional<Films> filmOptional = filmRepository.findById(id);
//        if (filmOptional.isPresent()) {
//            Films existingFilm = filmOptional.get();
//            if (dto.getTitle() != null && dto.getDirector() != null && dto.getRelease_date() != null) {
//                existingFilm.setTitle(dto.getTitle());
//                existingFilm.setDirector(dto.getDirector());
//                existingFilm.setRelease_date(dto.getRelease_date());
//
//                if (dto.getDescrip() != null) {
//                    existingFilm.setDescrip(dto.getDescrip());
//                }
//
//                if (dto.getGenres() != null) {
//                    List<Genre> genreList = genreRepository.findByNameIn(dto.getGenres());
//                    if (!genreList.isEmpty()) {
//                        existingFilm.getGenres().clear();
//                        existingFilm.setGenres(new HashSet<>(genreList));
//                    }
//                }
//
//                this.filmRepository.save(existingFilm);
//                return ResponseEntity.ok("Updated");
//            } else {
//                return ResponseEntity.badRequest().body("Film details cannot be null");
//            }
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
    public FilmDto mapToDto(Films film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setTitle(film.getTitle());
        dto.setDirector(film.getDirector());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDescrip(film.getDescrip());
        dto.setGenres(film.getGenres().stream()
                .map(genre -> genre.getName())
                .collect(Collectors.toList()));
        dto.setImageData(film.getImageData()); // Assuming you don't want to expose the image data
        dto.setVideoData(film.getVideoData()); // Assuming you don't want to expose the video data
        return dto;
    }

    public List<FilmDto> mapToDtoList(List<Films> films) {
        return films.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


}
