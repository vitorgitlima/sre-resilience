package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.gateway.FeatureToggleGateway;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
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
public class FeatureToggleGatewayImplTest {

    @Mock
    private CrudRepository repositoryMock;
    @InjectMocks
    private FeatureToggleGateway featureToggleGateway = new FeatureToggleGatewayImpl();

    @Test
    void findById_ReturnValidValue() {
        //Arrange
        final int id = 1;
        Optional<DemoSRE> demoSREMock = Optional.of(new DemoSRE(1, "a"));
        ;
        when(repositoryMock.findById(anyInt())).thenReturn(demoSREMock);

        //Act
        Optional<DemoSRE> result = featureToggleGateway.findByIdWithFeatureToggle(id);

        //Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isNotNull();
        verify(repositoryMock, times(1)).findById(anyInt());
    }

    @Test
    void findById_ObjectNotFound() {
        // Arrange
        when(repositoryMock.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        Optional<DemoSRE> result = featureToggleGateway.findByIdWithFeatureToggle(anyInt());

        // Assert
        assertThat(result).isEmpty();
        verify(repositoryMock, times(1)).findById(anyInt());
    }

}
