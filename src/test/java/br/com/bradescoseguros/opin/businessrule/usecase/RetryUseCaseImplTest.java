package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.BadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.RetryGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExtraStatusCode;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RetryUseCaseImplTest {

    @Mock
    private RetryGateway mockGateway;

    @Mock
    private MessageSourceService mockMessageSourceService;

    @InjectMocks
    private RetryUseCase useCase = new RetryUseCaseImpl();

    private static final String MESSAGE_MOCK = "teste";


    @Test
    @Tag("unit")
    void getDemoSRE_ReturnValidValue() throws Throwable {
        //Arrange
        final int id = 1;
        Optional<DemoSRE> demoSREMock = Optional.of(DummyObjectsUtil.newInstance(DemoSRE.class));

        when(mockGateway.findByIdWithRetry(anyInt())).thenReturn(demoSREMock);

        //Act
        DemoSRE result = useCase.getDemoSREWithRetry(id);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        verify(mockGateway, times(1)).findByIdWithRetry(anyInt());
    }

    @Test
    @Tag("unit")
    void getDemoSRE_ThrowsDemoSRENoContentException() {
        //Arrange
        final int id = 1;

        when(mockMessageSourceService.getMessage(anyString())).thenReturn(MESSAGE_MOCK);
        when(mockGateway.findByIdWithRetry(anyInt())).thenReturn(Optional.empty());

        //Act
        NoContentException exception = Assertions.assertThrows(NoContentException.class, () -> useCase.getDemoSREWithRetry(id));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(MESSAGE_MOCK);
        verify(mockGateway, times(1)).findByIdWithRetry(anyInt());
    }

    @Test
    void externalApiCallWithRetry_SuccessfullyExecuted() {
        //Arrange
        final String resultMock = "ok";

        when(mockGateway.externalApiCallWithRetry(any(ExtraStatusCode.class))).thenReturn(resultMock);

        //Act
        useCase.externalApiCallWithRetry(ExtraStatusCode.OK);

        //Assert
        verify(mockGateway, times(1)).externalApiCallWithRetry(ExtraStatusCode.OK);
    }

    @Test
    void externalApiCallWithRetry_ThrowsDemoSREBadRequestException() {
        //Arrange
        final String resultMock = "ok";
        final String messageError = "O status informado não é suportado pela aplicação.";

        //Act
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> useCase.externalApiCallWithRetry(null));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(messageError);
        verify(mockGateway, times(0)).externalApiCallWithRetry(any());
    }

    @Test
    void externalApiCallWithRetryAndCircuitBreaker_SuccessfullyExecuted() {
        //Arrange
        final String resultMock = "ok";

        when(mockGateway.externalApiCallWithRetryAndCircuitBreaker(any(ExtraStatusCode.class))).thenReturn(resultMock);

        //Act
        useCase.externalApiCallWithRetryAndCircuitBreaker(ExtraStatusCode.OK);

        //Assert
        verify(mockGateway, times(1)).externalApiCallWithRetryAndCircuitBreaker(ExtraStatusCode.OK);
    }

    @Test
    void externalApiCallWithRetryAndCircuitBreaker_ThrowsDemoSREBadRequestException() {
        //Arrange
        final String resultMock = "ok";
        final String messageError = "O status informado não é suportado pela aplicação.";

        //Act
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> useCase.externalApiCallWithRetryAndCircuitBreaker(null));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(messageError);
        verify(mockGateway, times(0)).externalApiCallWithRetryAndCircuitBreaker(any());
    }
}
