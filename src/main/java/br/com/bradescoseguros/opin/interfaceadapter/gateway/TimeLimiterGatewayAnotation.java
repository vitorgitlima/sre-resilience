package br.com.bradescoseguros.opin.interfaceadapter.gateway;


import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class TimeLimiterGatewayAnotation {

    @Autowired
    private CrudRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @TimeLimiter(name = "limiterApi")
    public CompletableFuture<String> externalApiCallWithTimeLimiter() {

        final String fullURL = "http://localhost:8081/api/sre/v1/extra/delay";

        return CompletableFuture.supplyAsync(() -> restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody());
    }

    @TimeLimiter(name = "limiterDb")
    public CompletableFuture<Optional<DemoSRE>> findByIdWithTimeLimiter(Integer id) {
       return CompletableFuture.supplyAsync(() -> repository.findById(id));
    }
}

