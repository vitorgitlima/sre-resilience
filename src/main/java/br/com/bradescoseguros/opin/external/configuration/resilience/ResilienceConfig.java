package br.com.bradescoseguros.opin.external.configuration.resilience;

import br.com.bradescoseguros.opin.businessrule.exception.RetriableBaseException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRENoContentException;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class ResilienceConfig {

//    @Bean
//    public RetryRegistry retryRegistry() {
//        RetryConfig retryConfig = RetryConfig.custom()
//                .maxAttempts(5)
//                .failAfterMaxAttempts(true)
//                .waitDuration(Duration.ofSeconds(3))
//                .retryExceptions(DemoSRENoContentException.class)
//                .build();
//
//        return RetryRegistry.custom()
//                .addRetryConfig("CosmoRetry", retryConfig)
//                .build();
//    }

//    @Bean
//    public RetryConfigCustomizer retryConfigCustomizer() {
//        return RetryConfigCustomizer.of("CosmoRetry", builder -> {
//            builder.maxAttempts(2);
//            builder.retryOnException(e -> e instanceof DemoSRENoContentException);
//            builder.waitDuration(Duration.ofSeconds(3));
//            builder.failAfterMaxAttempts(true);
//        });
//    }

    @Bean
    public RegistryEventConsumer<Retry> myRetryRegistryEventConsumer() {

        return new RegistryEventConsumer<Retry>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher()
                        .onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {

            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {

            }
        };
    }
}
