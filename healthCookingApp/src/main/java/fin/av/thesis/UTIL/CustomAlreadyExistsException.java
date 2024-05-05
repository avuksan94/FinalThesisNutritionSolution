package fin.av.thesis.UTIL;

public class CustomAlreadyExistsException extends RuntimeException{
    public CustomAlreadyExistsException(String message) {
        super(message);
    }
}