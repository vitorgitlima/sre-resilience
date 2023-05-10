package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.exception.NotFoundException;
import br.com.bradescoseguros.opin.businessrule.exception.BadRequestException;
import br.com.bradescoseguros.opin.businessrule.exception.RegistryAlreadyExistsException;
import br.com.bradescoseguros.opin.businessrule.gateway.CrudGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.businessrule.validator.DemoSREValidator;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrudUseCaseImplTest {

    @Mock
    private CrudGateway mockGateway;

    @Mock
    private DemoSREValidator mockValidator;

    @Mock
    private MessageSourceService mockMessageSourceService;

    @InjectMocks
    private CrudUseCase useCase = new CrudUseCaseImpl();

    private static final String MESSAGE_MOCK = "teste";

    @Test
    @Tag("unit")
    void getDemoSRE_ReturnValidValue() throws Throwable {
        //Arrange
        final int id = 1;
        Optional<DemoSRE> demoSREMock = Optional.of(DummyObjectsUtil.newInstance(DemoSRE.class));

        when(mockGateway.findById(anyInt())).thenReturn(demoSREMock);

        //Act
        DemoSRE result = useCase.getDemoSRE(id);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        verify(mockGateway, times(1)).findById(anyInt());
    }

    @Test
    @Tag("unit")
    void getDemoSRE_ThrowsDemoSRENoContentException() {
        //Arrange
        final int id = 1;

        when(mockMessageSourceService.getMessage(anyString())).thenReturn(MESSAGE_MOCK);
        when(mockGateway.findById(anyInt())).thenReturn(Optional.empty());

        //Act
        NoContentException exception = Assertions.assertThrows(NoContentException.class, () -> useCase.getDemoSRE(id));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(MESSAGE_MOCK);
        verify(mockGateway, times(1)).findById(anyInt());
    }

    @Test
    @Tag("unit")
    void insertDemoSRE_SuccessfullyExecuted() {
        //Arrange
        DemoSRE demoSRE = new DemoSRE();
        demoSRE.setId(1);
        when(mockGateway.findById(anyInt())).thenReturn(Optional.empty());
        doNothing().when(mockGateway).insertDemoSRE(any());
        doNothing().when(mockValidator).execute(any());

        //Act
        useCase.insertDemoSRE(demoSRE);

        //Assert
        verify(mockGateway, times(1)).findById(anyInt());
        verify(mockGateway, times(1)).insertDemoSRE(any());
        verify(mockValidator, times(1)).execute(any());
    }

    @Test
    @Tag("unit")
    void insertDemoSRE_ThrowsDemoSRERegistryAlreadyExistsException() {
        //Arrange
        DemoSRE demoSRE = new DemoSRE();
        demoSRE.setId(1);
        final String messageError = "O ID informado na inserção DemoSRE já existe na base de dados";

        when(mockGateway.findById(anyInt())).thenReturn(Optional.of(new DemoSRE()));
        doNothing().when(mockValidator).execute(any());

        //Act
        RegistryAlreadyExistsException exception = Assertions.assertThrows(RegistryAlreadyExistsException.class, () -> useCase.insertDemoSRE(demoSRE));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(messageError);
        verify(mockGateway, times(1)).findById(anyInt());
        verify(mockGateway, times(0)).insertDemoSRE(any());
    }

    @Test
    @Tag("unit")
    void updateDemoSRE_SuccessfullyExecuted() {
        //Arrange
        DemoSRE demoSRE = new DemoSRE();
        demoSRE.setId(1);
        when(mockGateway.findById(anyInt())).thenReturn(Optional.of(demoSRE));
        doNothing().when(mockGateway).updateDemoSRE(any());
        doNothing().when(mockValidator).execute(any());

        //Act
        useCase.updateDemoSRE(demoSRE);

        //Assert
        verify(mockGateway, times(1)).findById(anyInt());
        verify(mockGateway, times(1)).updateDemoSRE(any());
        verify(mockValidator, times(1)).execute(any());
    }

    @Test
    @Tag("unit")
    void updateDemoSRE_ThrowsNotFoundException() {
        //Arrange
        DemoSRE demoSRE = new DemoSRE();
        demoSRE.setId(1);

        when(mockMessageSourceService.getMessage(anyString())).thenReturn(MESSAGE_MOCK);
        when(mockGateway.findById(anyInt())).thenReturn(Optional.empty());
        doNothing().when(mockValidator).execute(any());

        //Act
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.updateDemoSRE(demoSRE));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(MESSAGE_MOCK);
        verify(mockGateway, times(1)).findById(anyInt());
        verify(mockGateway, times(0)).updateDemoSRE(any());
    }

    @Test
    @Tag("unit")
    void removeDemoSRE_SuccessfullyExecuted() {
        //Arrange
        final int id = 1;

        when(mockGateway.findById(anyInt())).thenReturn(Optional.of(new DemoSRE()));
        doNothing().when(mockGateway).removeDemoSRE(any());

        //Act
        useCase.removeDemoSRE(id);

        //Assert
        verify(mockGateway, times(1)).findById(anyInt());
        verify(mockGateway, times(1)).removeDemoSRE(any());
    }

    @Test
    @Tag("unit")
    void removeDemoSRE_ThrowsNotFoundException() {
        //Arrange
        final int id = 1;

        when(mockMessageSourceService.getMessage(anyString())).thenReturn(MESSAGE_MOCK);
        when(mockGateway.findById(anyInt())).thenReturn(Optional.empty());

        //Act
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.removeDemoSRE(id));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(MESSAGE_MOCK);
        verify(mockGateway, times(1)).findById(anyInt());
        verify(mockGateway, times(0)).removeDemoSRE(any());
    }

    @Test
    void externalApiCall_SuccessfullyExecuted() {
        //Arrange
        final String resultMock = "ok";

        when(mockGateway.externalApiCall(any(ExtraStatusCode.class))).thenReturn(resultMock);

        //Act
        useCase.externalApiCall(ExtraStatusCode.OK);

        //Assert
        verify(mockGateway, times(1)).externalApiCall(ExtraStatusCode.OK);
    }

    @Test
    void externalApiCall_ThrowsDemoSREBadRequestException() {
        //Arrange
        final String resultMock = "ok";
        final String messageError = "O status informado não é suportado pela aplicação.";

        //Act
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> useCase.externalApiCall(null));

        //Assert
        assertThat(exception.getMessage()).isEqualTo(messageError);
        verify(mockGateway, times(0)).externalApiCall(any());
    }
}