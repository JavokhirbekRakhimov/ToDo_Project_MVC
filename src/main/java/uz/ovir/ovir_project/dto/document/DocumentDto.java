package uz.ovir.ovir_project.dto.document;

import lombok.*;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.entity.MyFile;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.entity.enums.DocumentStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class DocumentDto {
    @Id
    private UUID id=UUID.randomUUID();
    private Long orderNumber;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate doneDate;
    private LocalDateTime createAt;
    private Boolean local;
    private List<UserDto>users;

    private List<MyFile>files;

    private DocumentStatus status;

}
