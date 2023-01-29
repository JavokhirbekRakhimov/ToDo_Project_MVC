package uz.ovir.ovir_project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.ovir.ovir_project.entity.Role;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.entity.enums.Gender;
import uz.ovir.ovir_project.entity.enums.RoleEnum;
import uz.ovir.ovir_project.repository.RoleRepository;
import uz.ovir.ovir_project.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    @Value(value = "${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    private final RoleRepository roleRepository;
    private final MyPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        if (ddl!=null && ddl.contains("create")){
            Role roleAdmin=Role.builder().name(RoleEnum.ADMIN).build();
            Role saveAdmin = roleRepository.save(roleAdmin);
            Role roleUser=Role.builder().name(RoleEnum.USER).build();
            Role saveUser = roleRepository.save(roleUser);
            Role roleBoss=Role.builder().name(RoleEnum.BOSS).build();
            Role saveBoss = roleRepository.save(roleBoss);

            User admin=new User(passwordEncoder.passwordEncoder().encode("admin"),"Javokhirbek","Rakhimov","admin","javohirbekrakhimov@gmail.com", Gender.MALE, saveAdmin);
            admin.setPhone("+998997834961");
            admin.setBirthDay(LocalDate.of(1999,5,10));
            User save = userRepository.save(admin);
            save.setOrderNumber(1);
            userRepository.save(save);


        }

    }
}
