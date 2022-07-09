package cl.zeruscode.treip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TreipApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreipApplication.class, args);
	}

}
