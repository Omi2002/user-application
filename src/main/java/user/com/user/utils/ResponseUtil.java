package user.com.user.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<GenericResponse<T>> build(
            HttpStatus status, String message, T body) {

        GenericResponse<T> response = new GenericResponse<>(
                status.value(),
                message,
                body
        );

        return ResponseEntity.status(status).body(response);
    }
}
