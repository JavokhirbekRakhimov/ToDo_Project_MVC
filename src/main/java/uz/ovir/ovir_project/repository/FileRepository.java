package uz.ovir.ovir_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ovir.ovir_project.entity.MyFile;

import java.util.UUID;

public interface FileRepository extends JpaRepository<MyFile, UUID> {
}
