package br.com.bradescoseguros.opin.businessrule.gateway;

public interface DemoSRETimeLimiterGateway {

    String externalApiCallTimeLimiter();

    String externalApiCallTimeLimiterWithRetry();
}
