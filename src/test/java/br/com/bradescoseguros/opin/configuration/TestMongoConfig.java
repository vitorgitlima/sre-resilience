package br.com.bradescoseguros.opin.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

@Configuration
public class TestMongoConfig {
    private static final String CONNECTION_STRING = "mongodb://%s:%d";

    @Bean
    @Primary
    public MongoTemplate mongoTemplate(@Value("${spring.data.mongodb.port}") int mongoPort, @Value("${spring.data.mongodb.host}") String host) {
        return new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, host, mongoPort)), "test");
    }
}
