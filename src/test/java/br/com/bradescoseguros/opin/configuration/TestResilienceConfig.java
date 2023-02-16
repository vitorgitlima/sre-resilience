package br.com.bradescoseguros.opin.configuration;

import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@TestConfiguration
public class TestResilienceConfig {

    private final static String RETRY_COSMO_CONFIG = "cosmoRetry";
    private final static String RETRY_API_CONFIG = "apiRetry";
    private final static String CB_COSMO_CONFIG = "cosmoCircuitBreaker";
    private final static String CB_API_CONFIG = "apiCircuitBreaker";

    @Bean(name = RETRY_COSMO_CONFIG)
    public RetryConfigCustomizer cosmoRetry() {
        return RetryConfigCustomizer.of(RETRY_COSMO_CONFIG, builder -> {
            builder.maxAttempts(2);
            builder.waitDuration(Duration.ofMillis(100));
        });
    }

    @Bean(name = RETRY_API_CONFIG)
    public RetryConfigCustomizer apiRetry() {
        return RetryConfigCustomizer.of(RETRY_API_CONFIG, builder -> {
            builder.maxAttempts(2);
            builder.waitDuration(Duration.ofMillis(100));
        });
    }

    @Bean(name = CB_COSMO_CONFIG)
    public CircuitBreakerConfigCustomizer cosmoCircuitBreaker() {
        return CircuitBreakerConfigCustomizer.of(CB_COSMO_CONFIG, builder -> {
            builder.automaticTransitionFromOpenToHalfOpenEnabled(false);
            builder.minimumNumberOfCalls(3);
            builder.permittedNumberOfCallsInHalfOpenState(1);
        });
    }

    @Bean(name = CB_API_CONFIG)
    public CircuitBreakerConfigCustomizer apiCircuitBreaker() {
        return CircuitBreakerConfigCustomizer.of(CB_API_CONFIG, builder -> {
            builder.automaticTransitionFromOpenToHalfOpenEnabled(false);
            builder.minimumNumberOfCalls(3);
            builder.permittedNumberOfCallsInHalfOpenState(1);
        });
    }
}
