package br.com.bradescoseguros.opin.external.configuration.threadpoolbulkhead;

import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class ThreadPoolBulkheadConfiguration {

    @Value("${resilience4j.thread-pool-bulkhead.configs.default.maxThreadPoolSize:2}")
    private int maxThreadPoolSize;

    @Value("${resilience4j.thread-pool-bulkhead.configs.default.coreThreadPoolSize:2}")
    private int coreThreadPoolSize;

    @Value("${resilience4j.thread-pool-bulkhead.configs.default.queueCapacity:2}")
    private int queueCapacity;

    @Value("${resilience4j.thread-pool-bulkhead.configs.default.keepAliveDuration:20}")
    private int keepAliveDuration;

    @Bean
    public ThreadPoolBulkhead threadPoolBulkhead() {
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(maxThreadPoolSize)
                .coreThreadPoolSize(coreThreadPoolSize)
                .queueCapacity(queueCapacity)
                .keepAliveDuration(Duration.ofMillis(keepAliveDuration))
                .build();
        return ThreadPoolBulkhead.of("ThreadPoolBulkhead", config);
    }
}
