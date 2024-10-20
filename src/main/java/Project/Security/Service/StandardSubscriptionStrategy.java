package Project.Security.Service;

import Project.Security.Entity.User;
import Project.Security.Entity.Subscribtion;
import Project.Security.Repository.SubscriptionStrategy;
import Project.Security.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StandardSubscriptionStrategy implements SubscriptionStrategy {
    private final UserRepository userRepository;
    private static final Long ADMIN_ID = 1L;
    @Override
    public void processSubscription(User user, Subscribtion subscription) {
        if (user.getBalans() < subscription.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalans(user.getBalans() - subscription.getAmount());
        user.setSubscribtion(subscription);
        userRepository.save(user);
        transferFundsToAdmin(subscription.getAmount());
    }

    private void transferFundsToAdmin(int amount) {
        User admin = userRepository.findById(ADMIN_ID).orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.setBalans(admin.getBalans() + amount);
        userRepository.save(admin);
    }
}
