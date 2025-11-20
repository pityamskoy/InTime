package team.capybara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAutoConfiguration
@EnableAsync
@ComponentScan(basePackages = {"team.capybara.backend.spring"})
@EntityScan(basePackages = {"team.capybara.backend.spring.entities"})
@EnableJpaRepositories(basePackages = {"team.capybara.backend.spring.controllers.repositories"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
