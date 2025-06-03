package user.com.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeatBookingRequest {

    @NotNull
    private String email;

    private String seatNo;

    private String seatBookingDate;
}

