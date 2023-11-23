package kusitms.gallae;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class GallaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GallaeApplication.class, args);
	}

}
