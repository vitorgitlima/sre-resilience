package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.TimeLimiterGateway;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeLimiterUsecaseImplTest {

    @Mock
    private TimeLimiterGateway mockGateway;

    @Mock
    private MessageSourceService mockMessageSourceService;

    @InjectMocks
    private TimeLimiterUsecase useCase = new TimeLimiterUsecaseImpl();

    private static final String MESSAGE_MOCK = "teste";

    @Test
    @Tag("unit")
    void getDemoSRE_ReturnValidValue() {
        //Arrange
        Optional<DemoSRE> demoSREMock = Optional.of(DummyObjectsUtil.newInstance(DemoSRE.class));

        when(mockGateway.findByIdWithTimeLimiter(anyInt())).thenReturn(demoSREMock);

        //Act
        DemoSRE result = useCase.getDemoSRE(1);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        verify(mockGateway, times(1)).findByIdWithTimeLimiter(anyInt());
    }

    @Test
    @Tag("unit")
    void getDemoSRE_ThrowsDemoSRENoContentException() {
        //Arrange

        when(mockMessageSourceService.getMessage(anyString())).thenReturn(MESSAGE_MOCK);
        when(mockGateway.findByIdWithTimeLimiter(anyInt())).thenReturn(Optional.empty());

        //Act
        NoContentException exception = Assertions.assertThrows(NoContentException.class, () -> useCase.getDemoSRE(1));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(MESSAGE_MOCK);
        verify(mockGateway, times(1)).findByIdWithTimeLimiter(anyInt());
    }

}