package uz.ovir.ovir_project.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentList<T>{

    private Integer count;
    private Integer page;
    private List<T> list;
}
