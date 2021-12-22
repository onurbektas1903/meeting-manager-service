package tr.com.obss.meetingmanager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import tr.com.common.exceptions.BusinessValidationException;
import tr.com.common.exceptions.ErrorMessage;
import tr.com.common.exceptions.NotFoundException;
import tr.com.common.exceptions.NotUniqueException;
import tr.com.common.exceptions.ObjectInUseException;
import tr.com.common.exceptions.ServiceNotAvailableException;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

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

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<ErrorMessage> handleUnknownHostException(
            UnknownHostException ex, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessage(
            SERVICE_UNAVAILABLE.value(), Collections.singleton("serviceNotFound"), ex.getMessage()),
            SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ObjectInUseException.class)
    public ResponseEntity<ErrorMessage> handleObjectInUseException(
            ObjectInUseException ex, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessage(
                ex.getCode(), ex.getErrList(), ex.getMessage()),
            CONFLICT);
    }
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorMessage> handleBusinessValidationException(
            BusinessValidationException ex, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessage(
               ex),
            BAD_REQUEST);
    }
    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<ErrorMessage> handleProviderException(
            ProviderException ex, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessage(
               ex),
            ex.getHttpStatus());
    }

    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ErrorMessage> handleNotUniqueException(
            NotUniqueException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex), CONFLICT);
    }

    @ExceptionHandler(ServiceNotAvailableException.class)
    public ResponseEntity<ErrorMessage> handleServiceNotAvailableException(
            ServiceNotAvailableException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(ex), NOT_FOUND);
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