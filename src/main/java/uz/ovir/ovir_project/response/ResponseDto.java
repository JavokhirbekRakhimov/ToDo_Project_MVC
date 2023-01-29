package uz.ovir.ovir_project.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto <T>{
    private Boolean success=false;
    private String message;
    private T obj;

    public ResponseDto(Boolean success) {
        this.success = success;
    }
}
