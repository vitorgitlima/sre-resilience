package br.com.bradescoseguros.opin.businessrule.usecase;

public interface BulkheadUsecase {

    String externalApiCallWithSemaphoreBulkhead();

    String externalApiCallWithSemaphoreBulkheadAndRetry();

    String externalApiCallWithThreadPoolBulkhead();

}
