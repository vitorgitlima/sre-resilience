package br.com.bradescoseguros.opin.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class TestRedisConfiguration {

    private static RedisServer redisServer;

    public TestRedisConfiguration(@Value("${spring.redis.port}") int redisPort) {
        if (redisServer == null) {
            redisServer = new RedisServer(redisPort);
        }
    }

    @PostConstruct
    public void postConstruct() {
        if (!redisServer.isActive()) {
            redisServer.start();
        }
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }

}