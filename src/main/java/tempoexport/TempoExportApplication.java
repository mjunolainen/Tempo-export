package tempoexport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TempoExportApplication {

    public static void main(String[] args) {
        SpringApplication.run(TempoExportApplication.class, args);
    }

}
