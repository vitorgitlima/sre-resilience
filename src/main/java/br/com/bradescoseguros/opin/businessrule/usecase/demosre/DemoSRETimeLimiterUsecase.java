package br.com.bradescoseguros.opin.businessrule.usecase.demosre;

public interface DemoSRETimeLimiterUsecase {
    String externalApiCall();
    String externalApiCallWithRetry();
}
