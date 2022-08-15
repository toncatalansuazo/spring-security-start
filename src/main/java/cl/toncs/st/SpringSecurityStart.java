package cl.toncs.st;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringSecurityStart {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityStart.class, args);
	}

}
