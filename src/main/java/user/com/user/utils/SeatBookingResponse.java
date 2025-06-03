package user.com.user.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatBookingResponse<T> {
    private String status;
    private String message;
    private T data;
}
