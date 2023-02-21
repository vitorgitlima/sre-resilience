package br.com.bradescoseguros.opin.external.configuration.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedisCacheErrorHandler extends SimpleCacheErrorHandler {

	@Override
	public void handleCacheGetError(final RuntimeException exception, final Cache cache, final Object key) {
		log.warn("Error getting from cache: " + exception.getMessage());
	}

	@Override
	public void handleCachePutError(final RuntimeException exception, final Cache cache, final Object key, final Object value) {
		log.warn("Unable to put into cache " + cache.getName() + " : " + exception.getMessage());
	}
}