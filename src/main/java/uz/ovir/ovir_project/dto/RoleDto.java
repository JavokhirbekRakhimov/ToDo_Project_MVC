package uz.ovir.ovir_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ovir.ovir_project.entity.enums.RoleEnum;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Integer id;
    @Enumerated(value = EnumType.STRING)
    private RoleEnum name;

}
