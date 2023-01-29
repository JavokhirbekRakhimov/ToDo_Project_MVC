package uz.ovir.ovir_project.entity;

import lombok.*;
import org.hibernate.Hibernate;
import uz.ovir.ovir_project.entity.enums.DocumentStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Data
public class Document {
    @Id
    private UUID id=UUID.randomUUID();
    private Long orderNumber;
    @Column(columnDefinition="TEXT")
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate doneDate;
    private LocalDateTime createAt=LocalDateTime.now();
    private Boolean local=true;
    @ManyToMany
    @ToString.Exclude
    private List<User>users;
    @OneToMany
    @ToString.Exclude
    private List<MyFile>files;
    @Enumerated(EnumType.STRING)
    private DocumentStatus status=DocumentStatus.JARAYONDA;

}
