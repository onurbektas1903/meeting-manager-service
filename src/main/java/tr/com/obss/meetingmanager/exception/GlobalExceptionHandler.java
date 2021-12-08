package tr.com.obss.meetingmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {
    //TODO Web requesti ne için kullanabilirim araştır
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(
            NotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex), NOT_FOUND);
    }

    @ExceptionHandler(MeetingOccupiedException.class)
    public ResponseEntity<ErrorMessage> handleMeetingOccupiedException(
            MeetingOccupiedException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex), CONFLICT);
    }
    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ErrorMessage> handleNotUniqueException(
            NotUniqueException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex), CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new ErrorMessage(new BusinessValidationException(ex.getMessage(),errors)),
                BAD_REQUEST);
    }
}