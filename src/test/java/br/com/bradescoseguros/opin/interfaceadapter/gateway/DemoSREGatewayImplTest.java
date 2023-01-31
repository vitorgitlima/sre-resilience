package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.domain.demosre.ExtraStatusCode;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.interfaceadapter.repository.DemoSRERepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemoSREGatewayImplTest {

    @Mock
    private DemoSRERepository repositoryMock;

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private DemoSREGateway gateway = new DemoSREGatewayImpl();

    @Test
    void findById() {
        //Arrange
        final int id = 1;
        Optional<DemoSRE> demoSREMock = Optional.of(DummyObjectsUtil.newInstance(DemoSRE.class));
        when(repositoryMock.findById(anyInt())).thenReturn(demoSREMock);

        //Act
        Optional<DemoSRE> result = gateway.findById(id);

        //Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isNotNull();
        verify(repositoryMock, times(1)).findById(anyInt());
    }

    @Test
    void insertDemoSRE() {
        //Arrange
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        when(repositoryMock.insert(any(DemoSRE.class))).thenReturn(demoSREMock);

        //Act
        gateway.insertDemoSRE(demoSREMock);

        //Assert
        verify(repositoryMock, times(1)).insert(demoSREMock);
    }

    @Test
    void updateDemoSRE() {
        //Arrange
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        when(repositoryMock.save(any(DemoSRE.class))).thenReturn(demoSREMock);

        //Act
        gateway.updateDemoSRE(demoSREMock);

        //Assert
        verify(repositoryMock, times(1)).save(demoSREMock);
    }

    @Test
    void removeDemoSRE() {
        //Arrange
        final int id = 1;
        doNothing().when(repositoryMock).deleteById(anyInt());

        //Act
        gateway.removeDemoSRE(id);

        //Assert
        verify(repositoryMock, times(1)).deleteById(id);
    }

    @Test
    void externalApiCall() {
        //Arrange
        final String resultMock = "ok";

        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(), eq(String.class))).thenReturn(new ResponseEntity<>(resultMock, HttpStatus.OK));

        //Act
        String result = restTemplateMock.exchange("", HttpMethod.GET, null, String.class).getBody();

        //Assert
        verify(restTemplateMock, times(1)).exchange(anyString(), any(HttpMethod.class), any(), eq(String.class));
        assertThat(result).isEqualTo(result);
    }
}