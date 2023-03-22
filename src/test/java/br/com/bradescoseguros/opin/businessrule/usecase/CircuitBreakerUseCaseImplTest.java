package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.BadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.CircuitBreakerGateway;
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
public class CircuitBreakerUseCaseImplTest {

    @Mock
    private CircuitBreakerGateway mockGateway;

    @Mock
    private MessageSourceService mockMessageSourceService;

    @InjectMocks
    private CircuitBreakerUseCase useCase = new CircuitBreakerUseCaseImpl();

    private static final String MESSAGE_MOCK = "teste";


    @Test
    @Tag("unit")
    void getDemoSRE_ReturnValidValue() {
        //Arrange
        final int id = 1;
        Optional<DemoSRE> demoSREMock = Optional.of(DummyObjectsUtil.newInstance(DemoSRE.class));

        when(mockGateway.findByIdWithCircuitBreaker(anyInt())).thenReturn(demoSREMock);

        //Act
        DemoSRE result = useCase.getDemoSREWithCircuitBreaker(id);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        verify(mockGateway, times(1)).findByIdWithCircuitBreaker(anyInt());
    }

    @Test
    @Tag("unit")
    void getDemoSRE_ThrowsDemoSRENoContentException() {
        //Arrange
        final int id = 1;

        when(mockMessageSourceService.getMessage(anyString())).thenReturn(MESSAGE_MOCK);
        when(mockGateway.findByIdWithCircuitBreaker(anyInt())).thenReturn(Optional.empty());

        //Act
        NoContentException exception = Assertions.assertThrows(NoContentException.class, () -> useCase.getDemoSREWithCircuitBreaker(id));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(MESSAGE_MOCK);
        verify(mockGateway, times(1)).findByIdWithCircuitBreaker(anyInt());
    }

    @Test
    void externalApiCallWithRetry_SuccessfullyExecuted() {
        //Arrange
        final String resultMock = "ok";

        when(mockGateway.externalApiCallWithCircuitBreaker(any(ExtraStatusCode.class))).thenReturn(resultMock);

        //Act
        useCase.externalApiCallWithCircuitBreaker(ExtraStatusCode.OK);

        //Assert
        verify(mockGateway, times(1)).externalApiCallWithCircuitBreaker(ExtraStatusCode.OK);
    }

    @Test
    void externalApiCallWithRetry_ThrowsDemoSREBadRequestException() {
        //Arrange
        final String messageError = "O status informado não é suportado pela aplicação.";

        //Act
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> useCase.externalApiCallWithCircuitBreaker(null));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(messageError);
        verify(mockGateway, times(0)).externalApiCallWithCircuitBreaker(any());
    }
}
