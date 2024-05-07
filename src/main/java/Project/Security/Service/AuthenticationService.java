package Project.Security.Service;

import Project.Security.Entity.Subscribtion;
import Project.Security.Repository.SubscribtionRepository;
import Project.Security.dto.*;
import Project.Security.Entity.Role;
import Project.Security.Entity.User;
import Project.Security.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SubscribtionRepository subscribtionRepository;

    //============================USERS==================================
    public AuthenticationResponse register(RegisterRequest request) {
        Subscribtion defaultSubscription = subscribtionRepository.findById(Long.valueOf(1))
                .orElseThrow(() -> new RuntimeException("Default subscription with id=1 not found"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .age(request.getAge())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .balans(10000)
                .subscribtion(defaultSubscription)
                .role(Role.USER)
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
                if (updatedUser.getRole() != null) {
                    existingUser.setRole(updatedUser.getRole());
                }
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

                existingUser.setId(id);
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
}
