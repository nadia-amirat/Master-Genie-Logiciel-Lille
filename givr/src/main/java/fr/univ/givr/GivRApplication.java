package fr.univ.givr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fr.univ.givr")
public class GivRApplication {

    public static void main(String[] args) {
        SpringApplication.run(GivRApplication.class, args);
    }

}
