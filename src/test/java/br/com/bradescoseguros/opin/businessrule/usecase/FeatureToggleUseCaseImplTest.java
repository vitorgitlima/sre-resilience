package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.gateway.CrudGateway;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import com.azure.spring.cloud.feature.management.FeatureManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNull;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeatureToggleUseCaseImplTest {

    @Mock
    private CrudGateway mockGateway;
    @Mock
    private FeatureManager featureManager;

    @InjectMocks
    private FeatureToggleUseCaseImpl featureToggleUseCase;

    @Test
    void getDemoSREWithToggleEnabled_ReturnValidValue() {
        // Arrange
        final int id = 2;
        Optional<DemoSRE> demoSREMock = Optional.of(DummyObjectsUtil.newInstance(DemoSRE.class));
        when(featureManager.isEnabledAsync("feature-a")).thenReturn(Mono.just(true));
//        when(mockGateway.findById(1)).thenReturn(Optional.of(new DemoSRE()));
        when(mockGateway.findById(anyInt())).thenReturn(demoSREMock);

        // Act
        ExecutionResult<DemoSRE> result = featureToggleUseCase.getDemoSREWithToggleEnabled(id);

        // Assert
        assertNotNull(result.getObject());
        assertNull(result.getErrorType());
        verify(featureManager).isEnabledAsync("feature-a");
        verify(mockGateway, times(1)).findById(anyInt());
    }
}
