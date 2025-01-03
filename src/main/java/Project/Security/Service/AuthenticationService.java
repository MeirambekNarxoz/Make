package Project.Security.Service;

import Project.Security.Entity.Subscribtion;
import Project.Security.Repository.*;
import Project.Security.dto.*;
import Project.Security.Entity.Role;
import Project.Security.Entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SubscribtionRepository subscribtionRepository;
    private final CommentRepository commentRepository;
    private final SubscriptionFactory subscriptionFactory;
    //============================USERS==================================
    public AuthenticationResponse register(RegisterRequest request) {
        Subscribtion defaultSubscription = subscribtionRepository.findById(Long.valueOf(1))
                .orElseThrow(() -> new RuntimeException("Default subscription with id=1 not found"));
        Role defaultRole = roleRepository.findById(2L).orElse(null);


        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .age(request.getAge())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .balans(10000)
                .subscribtion(defaultSubscription)
                .role(defaultRole)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public List<User> getAllUsers() {
        return this.repository.findAll();
    }

    public ResponseEntity<Void> deleteUserById(Long id) {
        if (this.repository.existsById(id)) {
            commentRepository.deleteByFilmsId(id);
            this.repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    public String updateUserById(Long id, User updatedUser) {
        if (this.repository.existsById(id)) {
            User existingUser = this.repository.findById(id).orElse(null);
            if (existingUser != null) {
                if (updatedUser.getFirstname() != null) {
                    existingUser.setFirstname(updatedUser.getFirstname());
                }
                if (updatedUser.getLastname() != null) {
                    existingUser.setLastname(updatedUser.getLastname());
                }
                if (updatedUser.getAge() != null) {
                    existingUser.setAge(updatedUser.getAge());
                }
                if (updatedUser.getEmail() != null) {
                    existingUser.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getPassword() != null) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }
                if (updatedUser.getSubscribtion() != null) {
                    Subscribtion subscription = subscribtionRepository.findById(updatedUser.getSubscribtion().getId())
                            .orElseThrow(() -> new RuntimeException("Subscription not found"));
                    existingUser.setSubscribtion(subscription);
                }
                this.repository.save(existingUser);
                return "Updated";
            } else {
                return "User not found";
            }
        } else {
            return "User not found";
        }
    }
    public List<Subscribtion> getAllSubscribtion() {
        return this.subscribtionRepository.findAll();
    }
    public ResponseEntity<User> getById(Long id) {
        return this.repository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    public ResponseEntity<Subscribtion> getSubById(Long id) {
        return this.subscribtionRepository.findById(id)
                .map(subscribtion -> ResponseEntity.ok(subscribtion))
                .orElse(ResponseEntity.notFound().build());
    }
    @Transactional
    public String processPayment(PaymentRequest paymentRequest) {
        User user = repository.findById(paymentRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Assuming that 'type' is the name of the subscription
        Subscribtion subscription = subscribtionRepository.findByName(paymentRequest.getType()).orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (user.getBalans() < subscription.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        SubscriptionStrategy strategy = subscriptionFactory.getStrategy(subscription);
        strategy.processSubscription(user, subscription);

        return "Payment processed successfully";
    }
    public User updateUserRole(Long userId, Role newRole) {
        // Получение пользователя и обновление роли без проверки на администратора
        User user = repository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRole(newRole);
        return repository.save(user);
    }



}
