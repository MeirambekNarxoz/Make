package Project.Security.Service;

import Project.Security.Entity.Subscribtion;
import Project.Security.Repository.SubscribtionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribtionService {
    private final SubscribtionRepository subscribtionRepository;

    public List<Subscribtion> getAllSubscriptions() {
        return subscribtionRepository.findAll();
    }
}
