package server.thn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ThnApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThnApplication.class, args);
	}

}
