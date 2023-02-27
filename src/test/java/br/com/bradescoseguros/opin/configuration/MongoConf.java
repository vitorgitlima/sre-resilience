package br.com.bradescoseguros.opin.configuration;

import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.UnknownHostException;

@RequiredArgsConstructor
@Configuration
public class MongoConf {

    private static final String CONNECTION_STRING = "mongodb://%s:%d";

    private MongodExecutable mongodExecutable;
    private MongoTemplate mongoTemplate;
    @Bean
    @Primary
    public MongoTemplate mongoTemplate() throws IOException {

        String ip = "localhost";
        int port = 27017;

        ImmutableMongodConfig mongodConfig = MongodConfig
                .builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();

        return new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, port)), "test");
    }

    @PreDestroy
    public void destroyMongo() {
        mongodExecutable.stop();
    }

}
