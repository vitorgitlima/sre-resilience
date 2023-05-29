package br.com.bradescoseguros.opin.external.configuration.mongodb;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import lombok.var;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class MongoDbConfiguration {

    private final MongoDbProperties mongoDbProperties;

    @Bean
    @Profile("!test")
    public MongoTemplate mongoTemplate() {
        var mongoCredential = MongoCredential.createCredential(mongoDbProperties.getUsername(), mongoDbProperties.getDatabase(), mongoDbProperties.getPassword().toCharArray());
        var mongoClientSettings = createMongoClientSettings(mongoCredential);

        return new MongoTemplate(MongoClients.create(mongoClientSettings), mongoDbProperties.getDatabase());
    }

    private MongoClientSettings createMongoClientSettings(MongoCredential mongoCredential) {
        var connectionString = new ConnectionString(mongoDbProperties.getUrl());

        Block<ConnectionPoolSettings.Builder> connectionPoolSettings = connectionPoolSettingsBuilder ->
                connectionPoolSettingsBuilder
                        .minSize(mongoDbProperties.getConnectionPoolMinSize())
                        .maxSize(mongoDbProperties.getConnectionPoolMaxSize())
                        .maintenanceFrequency(mongoDbProperties.getConnectionPoolMaintenanceFrequency(), TimeUnit.MINUTES)
                        .maintenanceInitialDelay(mongoDbProperties.getConnectionPoolMaintenanceInitialDelay(), TimeUnit.SECONDS)
                        .maxConnectionLifeTime(mongoDbProperties.getConnectionPoolMaxConnectionLifeTime(), TimeUnit.MINUTES)
                        .maxConnectionIdleTime(mongoDbProperties.getConnectionPoolMaxConnectionIdleTime(), TimeUnit.MINUTES)
                        .maxWaitTime(mongoDbProperties.getConnectionPoolMaxWaitTime(), TimeUnit.SECONDS);

        Block<ClusterSettings.Builder> clusterSettings = clusterSettingsBuilder ->
                clusterSettingsBuilder.applyConnectionString(connectionString)
                        .build();

        return MongoClientSettings.builder()
                .credential(mongoCredential)
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(connectionPoolSettings)
                .applyToClusterSettings(clusterSettings)
                .build();
    }
}