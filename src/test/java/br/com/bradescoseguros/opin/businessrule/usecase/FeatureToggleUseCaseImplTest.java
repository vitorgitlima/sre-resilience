package br.com.bradescoseguros.opin.businessrule.usecase;

import br.com.bradescoseguros.opin.businessrule.gateway.FeatureToggleGateway;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.domain.ExecutionResult;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import com.azure.spring.cloud.feature.management.FeatureManager;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeatureToggleUseCaseImplTest {

    @InjectMocks
    private FeatureToggleUseCaseImpl featureToggleUseCase;
    @Mock
    private FeatureToggleGateway mockGateway;
    @Mock
    private FeatureManager featureManager;


    @Test
    void getDemoSREWithToggleEnabled_ReturnValidValue() {
        // Arrange
        final int id = 2;
        Optional<DemoSRE> demoSREMock = Optional.of(new DemoSRE(2,"a"));
        when(featureManager.isEnabledAsync("feature-a")).thenReturn(Mono.just(true));
        when(mockGateway.findByIdWithFeatureToggle(anyInt())).thenReturn(demoSREMock);

        // Act
        ExecutionResult<DemoSRE> result = featureToggleUseCase.getDemoSREWithToggleEnabled(id);

        // Assert
        assertThat(result.getObject().getId()).isEqualTo(2);
        assertNotNull(result.getObject());
        assertNull(result.getErrorType());
        verify(featureManager).isEnabledAsync("feature-a");
        verify(mockGateway, times(1)).findByIdWithFeatureToggle(anyInt());
    }
}
