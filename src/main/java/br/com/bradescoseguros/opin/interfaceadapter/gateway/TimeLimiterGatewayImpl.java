package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.interfaceadapter.exception.TimeOutException;
import br.com.bradescoseguros.opin.businessrule.gateway.TimeLimiterGateway;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class TimeLimiterGatewayImpl implements TimeLimiterGateway {

    @Autowired
    private TimeLimiterGatewayAnotation timeLimiterGatewayAnotation;


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
            return timeLimiterGatewayAnotation.externalApiTimeLimiterThreadPool().get();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
            throw new GatewayException(e.getMessage());
        } catch (ExecutionException e) {
            log.error(e.getMessage());
            if(e.getCause() instanceof TimeoutException){
                throw new TimeOutException(e.getMessage());
            }
            throw new GatewayException(e);
        }

    }
}
