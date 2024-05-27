package fin.av.thesis.REST.Advice;

import fin.av.thesis.UTIL.CustomErrorResponse;
import fin.av.thesis.UTIL.CustomForbiddenException;
import fin.av.thesis.UTIL.CustomNotFoundException;
import fin.av.thesis.UTIL.CustomUnauthorizedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomNotFoundException.class)
    public Mono<ResponseEntity<CustomErrorResponse>> handleCustomNotFoundException(CustomNotFoundException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<CustomErrorResponse>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        CustomErrorResponse customErrorResponse = new CustomErrorResponse();
        customErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        customErrorResponse.setMessage("Validation failed");
        customErrorResponse.setTimestamp(LocalDateTime.now());
        customErrorResponse.setError(errors.toString());

        return Mono.just(new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<CustomErrorResponse>> handleGeneralException(Exception ex) {
        HttpStatus status = determineStatus(ex);
        return Mono.just(ResponseEntity.status(status).body(createErrorResponse(status, "An unexpected error occurred")));
    }

    private HttpStatus determineStatus(Exception ex) {
        if (ex instanceof CustomUnauthorizedException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof CustomForbiddenException) {
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private CustomErrorResponse createErrorResponse(HttpStatus status, String message) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setMessage(message);
        return errorResponse;
    }
}
