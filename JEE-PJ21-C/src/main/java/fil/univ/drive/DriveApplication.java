package fil.univ.drive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "fil.univ.drive")
public class DriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriveApplication.class, args);
	}

}
