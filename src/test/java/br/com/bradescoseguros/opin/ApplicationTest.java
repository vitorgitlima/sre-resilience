package br.com.bradescoseguros.opin;


import br.com.bradescoseguros.opin.external.configuration.mongodb.MongoDbConfiguration;
import br.com.bradescoseguros.opin.external.configuration.redis.RedisConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataMongoTest
@AutoConfigureDataMongo
class ApplicationTests {

    @InjectMocks
    private Application application;

    @Test
    void contextLoads() {
    }

    @Test
    @Tag("mutation")
    void postConstruct_withDefinedTimeZone_shouldRunSuccessfully() {

        //Arrange
        final String TIME_ZONE = "GMT-3";
        final TimeZone timeZone = TimeZone.getTimeZone(TIME_ZONE);

        ReflectionTestUtils.setField(application, "timeZone", TIME_ZONE);

        try(MockedStatic<TimeZone> response = mockStatic(TimeZone.class)) {

            response.when(() -> TimeZone.getTimeZone(any(String.class))).thenReturn(timeZone);

            //Act
            application.postConstruct();

            //Assertions
            response.verify(() -> TimeZone.setDefault(timeZone));
            response.verify(() -> TimeZone.getTimeZone(any(String.class)));

        }
    }
}
