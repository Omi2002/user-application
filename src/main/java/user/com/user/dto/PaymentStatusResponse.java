package user.com.user.dto;

import java.time.LocalDateTime;

import lombok.Data;
import user.com.user.enums.PaymentStatus;
@Data
public class PaymentStatusResponse {
     private String seatNo;
    private String email;
    private PaymentStatus status;
    private String reason;
    private LocalDateTime paymentTime;

}
