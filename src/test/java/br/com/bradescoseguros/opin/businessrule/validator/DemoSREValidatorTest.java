package br.com.bradescoseguros.opin.businessrule.validator;

import br.com.bradescoseguros.opin.businessrule.exception.ValidationException;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemoSREValidatorTest {

    @Mock
    private MessageSourceService messageSourceService;

    @InjectMocks
    private DemoSREValidator validator;

    @Test
    @Tag("unit")
    void execute_SuccessValidate() {
        //Arrange
        final DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);

        //Act
        validator.execute(demoSREMock);
    }

    @Test
    @Tag("unit")
    void execute_throwsValidadeExceptionWhenIdZero() {
        //Arrange
        final DemoSRE demoSREMock = DemoSRE.builder().id(0).value("teste").build();

        //Act
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> validator.execute(demoSREMock));

        //Assert
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).hasSize(1);
        verify(messageSourceService, times(1)).getMessage(anyString(), anyString());
    }

    @Test
    @Tag("unit")
    void execute_ThrowsValidadeExceptionWhenPayloadIsNull() {
        //Arrange
        final String errorMessage = "Mensagem teste";
        when(messageSourceService.getMessage(anyString())).thenReturn(errorMessage);

        //Act
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> validator.execute(null));

        //Assert
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).hasSize(1);
        verify(messageSourceService, times(1)).getMessage(anyString());
    }

    @Test
    @Tag("unit")
    void execute_ThrowsValidadeExceptionWhenPropertyIsMissing() {
        //Arrange
        final String errorMessage = "Mensagem teste";
        when(messageSourceService.getMessage(anyString(), anyString())).thenReturn(errorMessage);

        //Act
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> validator.execute(new DemoSRE()));

        //Assert
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).hasSize(2);
        verify(messageSourceService, times(2)).getMessage(anyString(), anyString());
    }

    @Test
    @Tag("unit")
    void execute_ThrowsValidadeExceptionWhenPropertyIsInvalid() {
        //Arrange
        final String errorMessage = "Mensagem teste";
        final DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        demoSREMock.setId(-1);

        when(messageSourceService.getMessage(anyString(), anyString())).thenReturn(errorMessage);

        //Act
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> validator.execute(demoSREMock));

        //Assert
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).hasSize(1);
        verify(messageSourceService, times(1)).getMessage(anyString(), anyString());
    }
}