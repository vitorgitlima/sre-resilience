package br.com.bradescoseguros.opin.external.configuration.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@Slf4j
@ConfigurationPropertiesScan
@Configuration
@EnableCaching
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.cache", name="enabled", havingValue="true", matchIfMissing = true)
@Import({RedisAutoConfiguration.class})
public class RedisConfiguration extends CachingConfigurerSupport {
#[[
	@Value("${spring.cache.redis.time-to-live:30}")
	private Integer timeToLeave;
]]#
	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		log.info("TTL: " + timeToLeave);
		return (builder) -> builder
						.withCacheConfiguration(RedisConstants.RESIDENTIAL_RANGE_CEP_CACHE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(timeToLeave)))
						.withCacheConfiguration(RedisConstants.RESIDENTIAL_CACHE_NAME, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(timeToLeave)));
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return new RedisCacheErrorHandler();
	}
}