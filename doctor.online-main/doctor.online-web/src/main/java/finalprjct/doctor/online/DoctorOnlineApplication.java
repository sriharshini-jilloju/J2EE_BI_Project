package finalprjct.doctor.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "finalprjct.doctor.online", "org.digital.online.ejb" })
@EnableJpaRepositories(basePackages = { "org.digital.online.ejb.repositories" })
@EntityScan(basePackages = "org.digital.online.ejb.entities")
public class DoctorOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorOnlineApplication.class, args);
	}

}
