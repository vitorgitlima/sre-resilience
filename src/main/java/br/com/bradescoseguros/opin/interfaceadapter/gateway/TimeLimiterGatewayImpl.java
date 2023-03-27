package br.com.bradescoseguros.opin.interfaceadapter.gateway;

import br.com.bradescoseguros.opin.businessrule.exception.GatewayException;
import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.interfaceadapter.exception.TimeOutException;
import br.com.bradescoseguros.opin.businessrule.gateway.TimeLimiterGateway;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class TimeLimiterGatewayImpl implements TimeLimiterGateway {


    @Autowired
    private TimeLimiterGatewayAnotation timeLimiterGatewayAnotation;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public String externalApiCallWithTimeLimiter() {
        return callExternalApiWithCompletableFuture();
    }

    @Override
    @Retry(name = "apiTimeLimiter")
    public String externalApiCallWithTimeLimiterAndRetry() {
        return callExternalApiWithCompletableFuture();
    }

    @Override
    public Optional<DemoSRE> findByIdWithTimeLimiter(Integer id) {
        return callDbWithCompletableFuture(id);
    }

    private String callExternalApiWithCompletableFuture() {
        log.info("Chamando externalApiCall com delay");
        try{
            return timeLimiterGatewayAnotation.externalApiCallWithTimeLimiter().get();
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

    private Optional<DemoSRE> callDbWithCompletableFuture(Integer id) {
        log.info("Chamando findById");
        try{
            return timeLimiterGatewayAnotation.findByIdWithTimeLimiter(id).get();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
            throw new GatewayException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            if(e.getCause() instanceof TimeoutException){
                throw new TimeOutException(e.getMessage());
            }
            throw new GatewayException(e);
        }
    }
}
