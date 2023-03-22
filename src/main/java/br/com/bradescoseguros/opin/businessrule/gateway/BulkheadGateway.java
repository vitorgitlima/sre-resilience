package br.com.bradescoseguros.opin.businessrule.gateway;

public interface BulkheadGateway {
    String externalApiCallWithBulkhead();

    String externalApiCallWithBulkheadAndRetry();

    String externalApiCallWithThreadPoolBulkhead();
}
