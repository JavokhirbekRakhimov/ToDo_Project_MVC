package uz.ovir.ovir_project.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ovir.ovir_project.entity.MyFile;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdate  {

    private UUID id;
    private String firstName;
    private String lastName;

    private String username;
    private String email;
    private String password;

    private String phone;
    private Integer orderNumber;

    private String birthDayStr;

    private Boolean active;

    private Boolean male=true;
    private Boolean female=false;
    private Integer roleId;

}
