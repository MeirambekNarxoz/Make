package Project.Security.Repository;

import Project.Security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsById(Long id);

    void deleteById(Long id);

    Optional <User> findById(Long id);
}
