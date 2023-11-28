package kusitms.gallae;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;


@EnableScheduling
@SpringBootApplication
public class GallaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GallaeApplication.class, args);
	}

}
