package user.com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import user.com.user.dto.SeatBookingRequest;
import user.com.user.model.SeatBooking;
import user.com.user.services.SeatBookingService;
import user.com.user.utils.ResponseMessages;
import user.com.user.utils.SeatBookingResponse;

@RestController
@RequestMapping("/api")
public class SeatBookingController {
    @Autowired
    private SeatBookingService seatBookingService;

    
    @PostMapping("/book")
    public SeatBookingResponse<SeatBooking> bookSeat(@RequestBody @Valid SeatBookingRequest request) {
        SeatBooking bookedSeat = seatBookingService.bookSeat(request);
        return new SeatBookingResponse<>(ResponseMessages.SUCCESS_SEAT_BOOK, ResponseMessages.SUCCESS_Message, bookedSeat);
    }
}
