package user.com.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import user.com.user.model.SeatBooking;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Integer>{
    SeatBooking findBySeatNoAndBookingDate(String seatNo, String bookingDate);
}
