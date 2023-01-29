package uz.ovir.ovir_project.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.ovir.ovir_project.dto.RoleDto;
import uz.ovir.ovir_project.entity.MyFile;
import uz.ovir.ovir_project.entity.Role;
import uz.ovir.ovir_project.entity.enums.Gender;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto  {

    private String firstName;
    private Boolean active;
    private UUID id;

    private String lastName;
    private String phone;

    private String username;
    private Integer orderNumber;

    private String email;

    private LocalDateTime createdAt;

    private Gender gender;
    private MyFile avatar;

    private RoleDto role;

}
