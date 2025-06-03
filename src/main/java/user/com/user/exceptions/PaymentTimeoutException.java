package user.com.user.exceptions;

public class PaymentTimeoutException extends RuntimeException{
    public PaymentTimeoutException(String message){
        super(message);
    }
}
