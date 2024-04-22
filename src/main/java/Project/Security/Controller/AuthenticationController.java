package Project.Security.Controller;

import Project.Security.auth.AuthenticationRequest;
import Project.Security.auth.AuthenticationResponse;
import Project.Security.Service.AuthenticationService;
import Project.Security.auth.RegisterRequest;
import Project.Security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authentication(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authentication(request));
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        this.service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable Long id,
                                                 @RequestBody User updatedUser) {
        String result = this.service.updateUserById(id, updatedUser);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        ResponseEntity<User> user = this.service.getById(id);
        return user;
    }
}

