package uz.ovir.ovir_project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file")
public class MyFile {
    @Id
    @Column(nullable = false,unique = true)
    private UUID id=UUID.randomUUID();
    private String filePath;
    private String originalName;
    private String contentType;
    private String generetedName;
    private Long size;
    private Integer orderNumber;

}
