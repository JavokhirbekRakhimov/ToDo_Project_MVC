package uz.ovir.ovir_project.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
public class UniversalException  extends RuntimeException {
    private String message;
    private HttpStatus status;

    public UniversalException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus;
    }

}
