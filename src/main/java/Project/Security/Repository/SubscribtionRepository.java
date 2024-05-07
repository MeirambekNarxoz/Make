package Project.Security.Repository;

import Project.Security.Entity.Subscribtion;
import Project.Security.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribtionRepository extends JpaRepository<Subscribtion, Long> {
    Optional<Subscribtion> findById(Long id);
}
