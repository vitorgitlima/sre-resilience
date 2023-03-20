package br.com.bradescoseguros.opin.configuration;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PreDestroy;
import java.io.IOException;

@RequiredArgsConstructor
@Configuration
public class MongoConfig {

    private static final String CONNECTION_STRING = "mongodb://%s:%d";

    private MongodExecutable mongodExecutable;

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() throws IOException {

        final String mongoServer = "localhost";
        final int mongoPort = 27020;

        ImmutableMongodConfig mongodConfig = MongodConfig
                .builder()
                .version(Version.Main.PRODUCTION)
            .net(new Net(mongoServer, mongoPort, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();

        return new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, mongoServer, mongoPort)), "test");
    }

    @PreDestroy
    public void stopMongo() {
        mongodExecutable.stop();
    }

}
