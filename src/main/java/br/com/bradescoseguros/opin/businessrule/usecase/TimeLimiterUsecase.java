package br.com.bradescoseguros.opin.businessrule.usecase;

public interface TimeLimiterUsecase {
    String externalApiCall();
    String externalApiCallWithRetry();
}
