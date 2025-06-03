package user.com.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import user.com.user.utils.GenericResponse;
import user.com.user.utils.ResponseUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DayForcastDataException.class)
    public ResponseEntity<?> handleDayForeCastException(DayForcastDataException de) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new GenericResponse<>(HttpStatus.NOT_FOUND.value(), "Unable to fetch the data", de.getMessage()));
    }

    @ExceptionHandler(CurrentDataException.class)
    public ResponseEntity<?> handleCurrentDataFetch(CurrentDataException cde) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new GenericResponse<>(HttpStatus.NOT_FOUND.value(), "Unable to fetch the data", cde.getMessage()));
    }

    @ExceptionHandler(PaymentTimeoutException.class)
    public ResponseEntity<?> handlePaymentFailedException(PaymentFailedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "PAYMENT_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(SeatBookingException.class)
    public ResponseEntity<?> handleSeatBookingException(SeatBookingException se) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "Seat Already Booked", se.getMessage()));
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<?> handleSeatBookingFailure(PaymentFailedException pe) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new GenericResponse<>(HttpStatus.NOT_ACCEPTABLE.value(), "Failed to book seat", pe.getMessage()));
    }

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<?> handleOAuthException(OAuthException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "OAuth Error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                        ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> UserNotFoundException(UserNotFoundException ex) {
        return ResponseUtil.build(HttpStatus.NOT_FOUND, ex.getMessage(), "Not Found");
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<GenericResponse<Object>> DuplicateUserException(DuplicateUserException ex) {
        return ResponseUtil.build(HttpStatus.CONFLICT, ex.getMessage(), "Not Found");
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<GenericResponse<Object>> InvalidUserDataException(InvalidUserDataException ex) {
        return ResponseUtil.build(HttpStatus.BAD_REQUEST, ex.getMessage(), "Not Found");
    }

    @ExceptionHandler(UserAuthorizationException.class)
    public ResponseEntity<GenericResponse<Object>> UserAuthorizationException(UserAuthorizationException ex) {
        return ResponseUtil.build(HttpStatus.FORBIDDEN, ex.getMessage(), "Not Found");
    }
}
