package br.com.bradescoseguros.opin;

import br.com.bradescoseguros.opin.configuration.TestMongoConfig;
import br.com.bradescoseguros.opin.configuration.TestRedisConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import({TestMongoConfig.class})
class ApplicationTests {

    @InjectMocks
    private Application application;

    @Test
    void contextLoads() {
        try {
            Application.main(new String[] {});
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
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