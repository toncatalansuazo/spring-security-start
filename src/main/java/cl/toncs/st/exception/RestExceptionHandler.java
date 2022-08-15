package cl.toncs.st.exception;

import cl.toncs.st.domain.error.ErrorModel;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var details = ex.getBindingResult().getFieldErrors().stream()
                                                                         .map(err -> String.format("Wrong field %s: %s. Value in request = %s.", err.getField(), err.getDefaultMessage(), err.getRejectedValue()))
                                                                         .collect(Collectors.joining("\n"));
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        ErrorModel error = null;
        if (ex.getCause() instanceof InvalidRoleException invalidRoleException) {
            error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", invalidRoleException.getCause().getCause().getLocalizedMessage());
        } else if (ex.getCause() instanceof JsonMappingException jsonMappingException) {
            error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", jsonMappingException.getLocalizedMessage());
        } else {
            error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", ex.getCause().getMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidRoleException.class, BadCredentialsException.class})
    public ResponseEntity<Object> handleInvalidRoleException(
        Exception ex, WebRequest request) {
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", ex.getCause().getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}