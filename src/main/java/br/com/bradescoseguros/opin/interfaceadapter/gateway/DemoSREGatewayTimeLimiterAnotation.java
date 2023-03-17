package br.com.bradescoseguros.opin.interfaceadapter.gateway;


import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class DemoSREGatewayTimeLimiterAnotation {

    @Autowired
    private RestTemplate restTemplate;

    @TimeLimiter(name = "orderService")
    public CompletableFuture<String> externalApiTimeLimiterThreadPool() {

        final String fullURL = "http://localhost:8081/api/sre/v1/extra/delay";

        return CompletableFuture.supplyAsync(() -> restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody());
    }

}

