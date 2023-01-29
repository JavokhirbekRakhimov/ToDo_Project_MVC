package uz.ovir.ovir_project;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class OvirProjectApplication {


    public static void main(String[] args) {
        SpringApplication.run(OvirProjectApplication.class, args);
    }

}
