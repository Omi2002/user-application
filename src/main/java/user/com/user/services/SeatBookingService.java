package user.com.user.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import user.com.user.config.SeatBookingConfig;
import user.com.user.dto.SeatBookingRequest;
import user.com.user.enums.PaymentStatus;
import user.com.user.exceptions.PaymentFailedException;
import user.com.user.exceptions.PaymentTimeoutException;
import user.com.user.exceptions.SeatBookingException;
import user.com.user.model.PaymentStatusRecord;
import user.com.user.model.SeatBooking;
import user.com.user.repository.PaymentStatusRepository;
import user.com.user.repository.SeatBookingRepository;
import user.com.user.utils.ResponseMessages;

@Service
public class SeatBookingService {
    @Autowired
    private SeatBookingRepository seatBookingRepository;

    @Autowired
    private SeatBookingConfig seatBookingConfig;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    // @Autowired
    // private SeatBookingRequest seatBookingRequest;

    public SeatBooking bookSeat(SeatBookingRequest seatBookingRequest) {
        String bookingDateStr = seatBookingRequest.getSeatBookingDate();

        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(bookingDateStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ResponseMessages.INVALID_DATE_FORMAT);
        }
       String formattedBookingDate = bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        if (seatBookingRepository.findBySeatNoAndBookingDate(seatBookingRequest.getSeatNo(),
                formattedBookingDate) != null) {
            throw new SeatBookingException(ResponseMessages.SEAT_BOOKED);
        }

        int timeoutCheck = new Random().nextInt(35) + 1;
        boolean paymentSuccess = new Random().nextBoolean();

        PaymentStatusRecord paymentStatus = new PaymentStatusRecord();
        paymentStatus.setEmail(seatBookingRequest.getEmail());
        paymentStatus.setSeatNo(seatBookingRequest.getSeatNo());
        paymentStatus.setPaymentTime(LocalDateTime.now());

        if (timeoutCheck > seatBookingConfig.getTimeoutLimit()) {
            paymentStatus.setStatus(PaymentStatus.TIMEOUT);
            paymentStatus.setReason(ResponseMessages.PAYMENT_TIMEOUT);
            paymentStatusRepository.save(paymentStatus);
            throw new PaymentTimeoutException(ResponseMessages.PAYMENT_TIMEOUT);
        }

        if (!paymentSuccess) {
            paymentStatus.setStatus(PaymentStatus.FAILED);
            paymentStatus.setReason(ResponseMessages.PAYMENT_FAILED);
            paymentStatusRepository.save(paymentStatus);
            throw new PaymentFailedException(ResponseMessages.PAYMENT_FAILED);
        }

        // Proceed to book the seat if not already booked
        SeatBooking newBooking = new SeatBooking();
        newBooking.setSeatNo(seatBookingRequest.getSeatNo());
        newBooking.setEmail(seatBookingRequest.getEmail());
        newBooking.setBookingDate(formattedBookingDate);

        SeatBooking savedBooking = seatBookingRepository.save(newBooking);

        paymentStatus.setStatus(PaymentStatus.SUCCESS);
        paymentStatus.setReason(ResponseMessages.PAYMENT_SUCCESS);
        paymentStatus.setBooking(savedBooking);
        paymentStatusRepository.save(paymentStatus);

        return savedBooking;
    }
}
