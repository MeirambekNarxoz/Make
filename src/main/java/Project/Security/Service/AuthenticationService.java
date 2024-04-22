package Project.Security.Service;

import Project.Security.auth.AuthenticationRequest;
import Project.Security.auth.AuthenticationResponse;
import Project.Security.auth.RegisterRequest;
import Project.Security.Role.Role;
import Project.Security.user.User;
import Project.Security.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .age(request.getAge())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
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


    public ResponseEntity<User> getById(Long id) {
        return this.repository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
}