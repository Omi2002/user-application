package user.com.user.model;

import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import user.com.user.enums.PaymentStatus;
@Entity
@Data
public class PaymentStatusRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNo;
    private String email;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String reason;

    private LocalDateTime paymentTime;

    @OneToOne
    private SeatBooking booking;
}
