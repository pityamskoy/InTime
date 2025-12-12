package team.capybara.backend.spring.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * {@code AsyncConfiguration} is the configuration class, which provides common handler for {@code @Async} methods
 */
@Configuration
@EnableAsync
@SuppressWarnings(value = {"unused"})
public class AsyncConfiguration {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setQueueCapacity(10);
        executor.setMaxPoolSize(10);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("AsyncTaskThread-");
        executor.initialize();

        return executor;
    }
}
