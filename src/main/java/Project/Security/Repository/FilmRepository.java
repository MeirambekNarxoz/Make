package Project.Security.Repository;

import Project.Security.Entity.Films;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Films, Long> {
    Optional<Films> findById(Long id);
}


