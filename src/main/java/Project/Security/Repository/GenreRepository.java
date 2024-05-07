package Project.Security.Repository;

import Project.Security.Entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findById(Long id);
    Set<Genre> findByNameIn(List<String> names );
    Optional<Genre> findByName(String name);

}

