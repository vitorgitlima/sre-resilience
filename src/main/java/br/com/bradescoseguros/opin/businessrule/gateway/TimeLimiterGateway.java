package br.com.bradescoseguros.opin.businessrule.gateway;

public interface TimeLimiterGateway {

    String externalApiCallTimeLimiter();

    String externalApiCallTimeLimiterWithRetry();
}
