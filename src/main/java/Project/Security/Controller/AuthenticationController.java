package Project.Security.Controller;

import Project.Security.Repository.UserRepository;
import Project.Security.dto.*;
import Project.Security.Service.AuthenticationService;
import Project.Security.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserRepository repository;
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        User user = repository.findByEmail(request.getEmail()).orElse(null);
        if(user != null){
            return ResponseEntity.badRequest().body("User already exists");
        }
        return ResponseEntity.ok().body(service.register(request));
    }

    //=====================USERS===========================
    @PostMapping("/login")
    public ResponseEntity<?> authentication(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response;
        try {
            response = service.authentication(request);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed!");
        }
        return ResponseEntity.ok().body(response);
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