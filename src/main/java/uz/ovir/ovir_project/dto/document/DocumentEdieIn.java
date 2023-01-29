package uz.ovir.ovir_project.dto.document;

import lombok.*;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.entity.MyFile;
import uz.ovir.ovir_project.entity.enums.DocumentStatus;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Builder
@Data
public class DocumentEdieIn {

    private UUID id;
    private String description;
    private Boolean local;
    private DocumentStatus status;
    private String startDateStr;
    private String endDateStr;
    private Boolean notLocal;
    private List<UUID>userIds;

}
