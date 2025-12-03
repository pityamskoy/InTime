package team.capybara.backend.spring.configs;

import io.micrometer.common.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
@SuppressWarnings(value = {"unused"})
public class CorsConfiguration implements WebMvcConfigurer {
    private final static String[] allowedMethods = {"HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"};
    private final static String[] controllerUrls = {"/categories", "/favorites", "/feed",
            "/images", "/interests", "/product_types", "/reviews", "/shops", "/users"};

    @Override
    public void addCorsMappings (@NonNull CorsRegistry registry) {
        String[] allowedOrigins = getAllowedOriginsFromEnv();

        for (String url : controllerUrls) {
            registry.addMapping(url).allowedOrigins(allowedOrigins).allowedMethods(allowedMethods);
        }
    }

    public String[] getAllowedOriginsFromEnv() {
        String cors = System.getenv("VSROK_CORS");

        if (cors == null) {
            return new String[0];
        }

        String[] allowedOrigins = { cors };

        return allowedOrigins;
    }
}
