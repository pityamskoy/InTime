package team.capybara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * {@code Main} class is the entrypoint of the backend, which based on {@link SpringApplication}.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"team.capybara.backend.spring"})
@EntityScan(basePackages = {"team.capybara.backend.spring.entities"})
@EnableJpaRepositories(basePackages = {"team.capybara.backend.spring.controllers.repositories"})
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}