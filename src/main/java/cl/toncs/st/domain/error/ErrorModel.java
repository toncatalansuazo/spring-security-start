package cl.toncs.st.domain.error;

import static java.time.ZoneOffset.UTC;

import java.time.LocalDateTime;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
public class ErrorModel {

    private final HttpStatus httpStatus;

    private final LocalDateTime timestamp;

    private final String message;

    private final String details;

    public ErrorModel(HttpStatus httpStatus, String message, String details) {
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now(UTC);
        this.message = message;
        this.details = details;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

}
