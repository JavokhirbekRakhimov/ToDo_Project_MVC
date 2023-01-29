package uz.ovir.ovir_project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.ovir.ovir_project.entity.enums.DocumentStatus;
import uz.ovir.ovir_project.repository.DocumentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CronJobs {
    private final DocumentRepository documentRepository;
    @Scheduled(cron = " 0 */5 * ? * * ")
    private void documentCheck(){
        documentRepository.cronJobNotDone(DocumentStatus.BAJARILMAGAN.name(), List.of(DocumentStatus.DELETE.name(),DocumentStatus.BAJARILGAN.name(),DocumentStatus.KECHIKTRIB_BAJARILGAN.name()));
    }
}
