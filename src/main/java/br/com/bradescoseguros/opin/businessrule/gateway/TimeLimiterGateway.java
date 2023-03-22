package br.com.bradescoseguros.opin.businessrule.gateway;

public interface TimeLimiterGateway {

    String externalApiCallWithTimeLimiter();

    String externalApiCallWithTimeLimiterAndRetry();
}
