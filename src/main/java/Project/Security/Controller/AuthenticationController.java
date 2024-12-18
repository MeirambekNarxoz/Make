package Project.Security.Controller;

import Project.Security.Entity.Comment;
import Project.Security.Entity.Role;
import Project.Security.Entity.Subscribtion;
import Project.Security.Repository.RoleRepository;
import Project.Security.Repository.UserRepository;
import Project.Security.Service.CommentService;
import Project.Security.Service.FilmFacade;
import Project.Security.Service.SubscribtionService;
import Project.Security.dto.*;
import Project.Security.Service.AuthenticationService;
import Project.Security.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final RoleRepository roleRepository;
    private final UserRepository repository;
    private final SubscribtionService subscribtionService;
    private final CommentService commentService;

//    1
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
//    2
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
//3
    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.service.getAllUsers();
        return ResponseEntity.ok(users);
    }
//    4
    @GetMapping("/subscribtion")
    public ResponseEntity<List<Subscribtion>> getAllSubscribtion() {
        List<Subscribtion> subscribtions = this.service.getAllSubscribtion();
        return ResponseEntity.ok(subscribtions);
    }
//    5
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        List<Comment> userComments = commentService.getAllCommentsByUserId(id);

        // Удаляем каждый комментарий
        for (Comment comment : userComments) {
            commentService.deleteCommentById(comment.getId());
        }

        this.service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
//6
@PutMapping("/user/{id}")
public ResponseEntity<String> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
    return ResponseEntity.ok(service.updateUserById(id, updatedUser));
}
//7
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        ResponseEntity<User> user = this.service.getById(id);
        return user;
    }
//    8
    @GetMapping("/subscribtion/{id}")
    public ResponseEntity<Subscribtion> getSubById(@PathVariable Long id) {
        ResponseEntity<Subscribtion> subscribtion = this.service.getSubById(id);
        return subscribtion;
    }
    @PostMapping("/process-payment")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            String result = service.processPayment(paymentRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscribtion>> getAllSubscriptions() {
        List<Subscribtion> subscriptions = subscribtionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }
    @PutMapping("/users/{id}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            User currentUser = repository.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));



            // Находим пользователя для обновления
            User userToUpdate = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User to update not found"));

            // Переключаем роль
            String currentRole = userToUpdate.getRole().getName();
            String newRole;

            if ("ADMIN".equals(currentRole)) {
                newRole = "USER"; // Если текущая роль ADMIN, меняем на USER
            } else {
                newRole = "ADMIN"; // Если текущая роль не ADMIN, меняем на ADMIN
            }

            Role role = roleRepository.findByName(newRole)
                    .orElseThrow(() -> new RuntimeException(newRole + " role not found"));

            userToUpdate.setRole(role); // Устанавливаем новую роль
            repository.save(userToUpdate); // Сохраняем изменения

            return ResponseEntity.ok("User role updated to " + newRole);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }







}