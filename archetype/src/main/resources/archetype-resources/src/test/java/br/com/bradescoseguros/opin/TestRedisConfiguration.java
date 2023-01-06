package br.com.bradescoseguros.opin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class TestRedisConfiguration {
	private RedisServer redisServer;

	public TestRedisConfiguration(@Value("${spring.redis.port}") final int redisPort) {
		this.redisServer = new RedisServer(redisPort);
	}

	@PostConstruct
	public void postConstruct() {
		redisServer.start();
	}

	@PreDestroy
	public void preDestroy() {
		redisServer.stop();
	}

}