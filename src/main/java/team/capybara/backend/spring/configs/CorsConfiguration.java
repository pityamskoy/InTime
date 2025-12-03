package team.capybara.backend.spring.configs;

import io.micrometer.common.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@SuppressWarnings(value = {"unused"})
public class CorsConfiguration implements WebMvcConfigurer {
    private final static String[] allowedMethods = {"HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"};
    private final static String[] allowedOrigins = {"http://localhost:3000"};
    private final static String[] controllerUrls = {"/categories", "/favorites", "/feed",
            "/images", "/interests", "/product_types", "/reviews", "/shops", "/users"};

    @Override
    public void addCorsMappings (@NonNull CorsRegistry registry) {
        for (String url : controllerUrls) {
            registry.addMapping(url).allowedOrigins(allowedOrigins).allowedMethods(allowedMethods);
        }
    }
}
