package user.com.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SEAT_BOOKING")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "seat_no", nullable = false)
    private String seatNo;

    @Column(name = "booking_date", nullable = false)
    
    private String bookingDate;

    @Column(name = "email", nullable = false)
    private String email;
}
