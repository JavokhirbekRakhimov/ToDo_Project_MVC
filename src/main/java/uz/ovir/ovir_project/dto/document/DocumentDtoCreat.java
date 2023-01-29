package uz.ovir.ovir_project.dto.document;

import lombok.*;
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
public class DocumentDtoCreat {

    private String description;
    private String startDateStr;
    private String endDateStr;
    private Boolean local=true;
    private Boolean notLocal=false;
    private List<UUID>userIds;


}
