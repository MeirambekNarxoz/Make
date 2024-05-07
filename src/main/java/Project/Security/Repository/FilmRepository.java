package Project.Security.Repository;

import Project.Security.Entity.Films;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmRepository extends JpaRepository<Films, Long> {
    Optional<Films> findById(Long id);
    List<Films> findByGenres_IdIn(Set<Long> genreIds);
}


