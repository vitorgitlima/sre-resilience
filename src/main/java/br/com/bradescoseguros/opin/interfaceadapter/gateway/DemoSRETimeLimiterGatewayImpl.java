package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.businessrule.exception.demosre.DemoSRETimeOutException;
import br.com.bradescoseguros.opin.businessrule.gateway.DemoSRETimeLimiterGateway;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class DemoSRETimeLimiterGatewayImpl implements DemoSRETimeLimiterGateway {

    @Autowired
    private DemoSREGatewayTimeLimiterAnotation demoSREGatewayTimeLimiterAnotation;


    @Override
    public String externalApiCallTimeLimiter() {
        return callExternalApiWithCompletableFuture();
    }

    @Override
    @Retry(name = "apiTimeLimiter")
    public String externalApiCallTimeLimiterWithRetry() {
        return callExternalApiWithCompletableFuture();
    }

    private String callExternalApiWithCompletableFuture() {
        try{
            return demoSREGatewayTimeLimiterAnotation.externalApiTimeLimiterThreadPool().get();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
            throw new GatewayException(e.getMessage());
        } catch (ExecutionException e) {
            log.error(e.getMessage());
            if(e.getCause() instanceof TimeoutException){
                throw new DemoSRETimeOutException(e.getMessage());
            }
            throw new GatewayException(e);
        }

    }
}
