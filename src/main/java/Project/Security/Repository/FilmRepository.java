package Project.Security.Repository;

import Project.Security.Entity.Films;
import Project.Security.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Films, Long> {
    Optional<Films> findById(Long id);
    @Query("SELECT f FROM Films f JOIN f.genres g WHERE g.id = :genreId")
    List<Films> findByGenreId(@Param("genreId") Long genreId);
}

