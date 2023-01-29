package uz.ovir.ovir_project.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDtoList<T>{
    private Boolean success=true;
    private String message;
    private List<T> list;
}
