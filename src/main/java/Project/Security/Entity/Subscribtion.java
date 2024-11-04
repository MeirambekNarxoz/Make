package Project.Security.Entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Valid
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_subscribtion")
public class Subscribtion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int amount;
}
