package uz.ovir.ovir_project.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Bakhromjon Fri, 2:49 PM 3/4/2022
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppError {
    private Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    private Integer status;
    private String error;
    private String message;
    private String path;

    @Builder
    public AppError(HttpStatus status, String message, WebRequest request) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}