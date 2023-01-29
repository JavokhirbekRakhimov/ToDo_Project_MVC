package uz.ovir.ovir_project.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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

public class UserCreate  {


    private String password;
    private String phone;

    private String firstName;

    private String lastName;

    private String username;

    private String email;


    private String birthDayStr;


    private Boolean male=true;
    private Boolean female=false;
    private Integer roleId;

}
