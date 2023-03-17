# Retry

## Overview
In some scenarios, when we call an external service (for example) we can receive an error. But, this error can be just a momentary error. So, if we call it again in a few seconds,
we can receive the correct response.

The retry pattern comes to acts in this specific scenario. Before we return an error response for our users,
we wait a few seconds and retry the last call to access the resource. This implementation will make retries a defined number of times, until we get the correct response or we reach the maximum number of retries. If we reach the limit of
number of times, then we return an error for our user saying that we can't reach the resource. The number of times and the wait time can be configured in each scenario.

In the picture below, we can see a scenario where an application tries to call a hosted service. In the first call, the application receives a 500 response. Then it tries again and receives another
500 status. But, when it tries for the third time, it can get the resource that it was trying to reach.
This pattern helps us to reduce the number of errors that are received by our users.

![Alt text](./docs/retry_example.png "Retry pattern")

But be careful, we need to analyze each scenario. In some cases, if we get an error and keep retrying, we can get more errors and cause more problems in the external service, for example.

To implement this pattern, we use resilience4j. More information can be found at [Resilience4 Retry Documentation](https://resilience4j.readme.io/docs/retry) :link:

## Setting up

### How to set Retry in your application

First we need to add a new dependency on pom.xml

```xml
        <!-- Resilience -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
            <version>${spring.boot.version}</version>
        </dependency>

        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-spring-boot2</artifactId>
            <version>1.7.1</version> <!-- For best results, ensure that your application is always running on the most recent LTS version it can handle -->
        </dependency>
```

Then add a new configuration in application.yml

```yaml
resilience4j.retry:
  configs: # An optional property that allows defining pre-configurations for Retry instances.
    default: # The name of the default pre-configuration being defined.
      maxAttempts: 3 # The maximum number of Retry attempts allowed for this configuration.
      waitDuration: 3s # The waiting time between Retry attempts.
      failAfterMaxAttempts: true # A flag that determines whether a MaxRetriesExceededException should be thrown after exceeding the maximum number of Retry attempts.
      retryExceptions: # A list of exceptions for which Retry should be applied
        - br.com.bradescoseguros.opin.businessrule.exception.RetriableBaseException
      ignoreExceptions: # A list of exceptions for which Retry should not be applied
        - io.github.resilience4j.circuitbreaker.CallNotPermittedException
  instances: # Allows defining Retry instances
    cosmoRetry: # The name of the Retry instance being defined
        maxAttempts: 5 # The maximum number of Retry attempts allowed for this instance. This property overrides the value defined in the default configuration, if any
```

Here's an example of how to use Retry in applications:

In this case, whenever the `findById` method or sub-method throws an exception that extends `RetriableBaseException` or is included in the `retryExceptions` property, a new attempt will be executed until the maximum limit is reached (in this case, 5 attempts)

```java
    @Retry(name = "cosmoRetry")
    public Optional<DemoSRE> findById(final Integer id) {
        return repository.findById(id);
    }
```