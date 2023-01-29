package uz.ovir.ovir_project.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ovir.ovir_project.dto.RoleDto;
import uz.ovir.ovir_project.entity.MyFile;
import uz.ovir.ovir_project.entity.enums.Gender;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDtoDocument {

    private String firstName;
    private UUID id;
    private String lastName;


    private RoleDto role;

}
