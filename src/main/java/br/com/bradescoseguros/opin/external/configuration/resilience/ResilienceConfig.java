package br.com.bradescoseguros.opin.external.configuration.resilience;

import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSREMaxRetriesExceededException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

@Slf4j
@Configuration
public class ResilienceConfig {

    @Bean
    public RegistryEventConsumer<Retry> myRetryRegistryEventConsumer() {

        return new RegistryEventConsumer<Retry>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher()
                        .onEvent(event -> {
                            int maxAttemps = entryAddedEvent.getAddedEntry().getRetryConfig().getMaxAttempts();
                            String logMessage = MessageFormat.format("[RETRY][{2}/{3}] Time: {0}, Name: {1}, Exception: {4}",
                                    event.getCreationTime(), event.getName(), event.getNumberOfRetryAttempts(), maxAttemps, event.getLastThrowable());
                            log.info(logMessage);

                            if (event.getNumberOfRetryAttempts() >= maxAttemps) {
                                throw new DemoSREMaxRetriesExceededException();
                            }
                        });
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
            }
        };
    }

    @Bean
    public RegistryEventConsumer<CircuitBreaker> myCircuitBreakerRegistryEventConsumer() {

        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher()
                        .onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {

            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {

            }

        };
    }
}
