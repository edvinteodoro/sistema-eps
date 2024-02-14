package gt.edu.cunoc.sistemaeps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SistemaEpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaEpsApplication.class, args);
	}

}
