package uz.ovir.ovir_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ovir.ovir_project.entity.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
