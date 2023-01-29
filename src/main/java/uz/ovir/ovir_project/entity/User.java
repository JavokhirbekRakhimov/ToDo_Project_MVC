package uz.ovir.ovir_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.ovir.ovir_project.entity.enums.Gender;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(nullable = false,unique = true)
    private UUID id;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false,unique = true)
    private String username;
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;
    private Integer orderNumber;
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    private LocalDate birthDay;
    @Column(nullable = false)
    private Boolean active;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ManyToOne
    private MyFile avatar;
    @ManyToOne
    private Role role;
    public User(String password, String userName) {
        this.password = password;
        this.username = userName;
        id=UUID.randomUUID();
    }

    public User(String password, String firstName, String lastName, String userName, String email, Gender gender, Role role) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = userName;
        this.email = email;
        this.gender = gender;
        this.role = role;
        id=UUID.randomUUID();
        active=true;
        createdAt=LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority>grantedAuthorities=new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));

        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
