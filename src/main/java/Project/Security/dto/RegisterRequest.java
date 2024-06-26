package Project.Security.dto;

import Project.Security.Entity.Subscribtion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private int age;
    private int balans;
    private String subscriptionName;
}
