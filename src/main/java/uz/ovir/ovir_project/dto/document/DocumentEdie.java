package uz.ovir.ovir_project.dto.document;

import lombok.*;
import uz.ovir.ovir_project.dto.userDto.UserCreate;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.entity.MyFile;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.entity.enums.DocumentStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Builder
@Data
public class DocumentEdie {

    private UUID id;
    private String description;
    private Boolean local;
    private List<UserDto>users;
    private List<MyFile>files;
    private DocumentStatus status;
    private String startDateStr;
    private String endDateStr;
    private Boolean notLocal;
    private List<UUID>userIds;

}
