package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkheadGatewayImplTest {

    @Mock
    private BulkheadThreadPoolGatewayAnotation bulkheadThreadPoolGatewayAnotation;

    @InjectMocks
    private BulkheadGatewayImpl gateway = new BulkheadGatewayImpl();


    @Test
    void externalApiCallTimeLimiter_ThrowsInterruptedException() throws ExecutionException, InterruptedException {

        CompletableFuture<String> mock = mock(CompletableFuture.class);

        when(mock.get()).thenThrow(new InterruptedException("teste"));
        when(bulkheadThreadPoolGatewayAnotation.externalApiBulkheadThreadPool()).thenReturn(mock);

        GatewayException gatewayException = assertThrows(GatewayException.class, () -> gateway.externalApiCallWithThreadPoolBulkhead());
        assertEquals("teste", gatewayException.getMessage());
        assertTrue(Thread.currentThread().isInterrupted());
    }

}