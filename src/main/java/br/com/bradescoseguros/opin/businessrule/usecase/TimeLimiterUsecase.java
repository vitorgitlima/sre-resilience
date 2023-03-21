package br.com.bradescoseguros.opin.businessrule.usecase;

public interface TimeLimiterUsecase {
    String externalApiCallWithTimeLimiter();
    String externalApiCallWithTimeLimiterAndRetry();
}
