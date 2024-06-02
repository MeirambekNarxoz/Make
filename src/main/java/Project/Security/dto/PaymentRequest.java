package Project.Security.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long userId;
    private int amount;
    private String type;
}
