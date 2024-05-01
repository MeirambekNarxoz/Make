package Project.Security.Repository;

import Project.Security.Entity.Films;
import Project.Security.Entity.Genre;
import Project.Security.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findById(Long id);
    List<Genre> findByNameIn(List<String> genres);
    Optional<Genre> findByName(String name);

}

