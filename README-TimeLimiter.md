# TimeLimiter

## Overview
The TimeLimiter is a resilience pattern that allows you to add a timeout to a method call. It's particularly useful for protecting your application from long-running or stuck operations that could cause resource exhaustion or other issues.

One main reason why we would do this is to ensure that we don’t make users or clients wait indefinitely. A slow service that does not give any feedback can be frustrating to the user.

## Benefits

* Improved application performance by preventing slow or hanging external services from blocking the calling thread.

* Enhanced resilience by providing more safety for your application when interacting with problematic external services.

* Flexibility to configure TimeLimiter to suit your specific use case, including custom timeout duration, cancellation of running tasks, and fallback behavior when triggered.

* Better user experience by preventing users from waiting indefinitely for a response and returning meaningful error messages or default responses.



![Alt text](./docs/time_limiter.png "TimeLimiter")



To implement this pattern, we use resilience4j. More information can be found at [Resilience4j TimeLimiter Documentation](https://resilience4j.readme.io/docs/timeout) :link:

## Implementation

`CopmpletableFuture`

When using TimeLimiter from Resilience4j, it's common to use CompletableFuture in order to execute the operation asynchronously and apply the timeout. This is because CompletableFuture provides a built-in timeout mechanism that can be used to implement the timeout functionality of TimeLimiter. We will discuss more about this in the usage topic.

## Setting up

### How to set TimeLimiter in your application

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
## Configuration

Then add a new configuration in application.yml

```yaml
resilience4j.timelimiter: # This line sets the root key for defining a TimeLimiter configuration with Resilience4j library.
  instances: # Defining TimeLimiter instances.
    timeLimiterService: # Declares a specific instance of a TimeLimiter with the name "timeLimiterService".
      timeoutDuration: 10s #This line sets the timeout duration for the timeLimiterService instance to 10 seconds. This means that if the protected method takes longer than 10 seconds to execute, the TimeLimiter will throw a TimeoutException.
      cancelRunningFuture: true # This line specifies whether to cancel the running future when the timeout occurs. If set to true, the future will be cancelled when the timeout occurs, otherwise it will be allowed to continue running.
```

## Usage and Details
Here's an example of how to use TimeLimiter in applications:

To understand the use of TimeLimiter, we need to talk a little more in detail about how to use `CompletableFuture`.

The reason for using `CompletableFuture` is to execute the external API call asynchronously, which means that the calling thread can continue executing other tasks while waiting for the API response. This can improve the overall capacity and responsiveness of the application.

### **Note**

For this implementation, we use a separete class, named `TimeLimiterGatewayAnotation`, which has `externalApiTimeLimiter`. This method implement TimeLimiter with `@TimeLimiter` annotation and have to be first call method on the class. 

If the time limit is exceeded or an exception occurs during the execution of the API call, the method will throw a RuntimeException or InterruptedException


### ***callExternalApiWithCompletableFuture***

The `callExternalApiWithCompletableFuture()` method in this example uses a `CompletableFuture` instance returned by the `timeLimiterGatewayAnotation` class to call an external API with a TimeLimiter, ensuring that the API call is completed within the specified time limit.

```java
   private String callExternalApiWithCompletableFuture() {
        try {
            return timeLimiterGatewayAnotation.externalApiTimeLimiter().get();
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        // you can also implement others Exceptions
    }
```

### ***externalApiTimeLimiter***
Here we have the annotation `@TimeLimiter` controlling the method, when the request exceeded the `timeoutDuration` configured in yaml, a `TimeoutException` will be thrown


```java
  @TimeLimiter(name = "timeLimiterService")
    public CompletableFuture<String> externalApiTimeLimiter() {

        final String fullURL = "http://localhost:8081/api/sre/v1/extra/delay";

        return CompletableFuture.supplyAsync(() -> restTemplate.exchange(fullURL, HttpMethod.GET, null, String.class).getBody());
    }

```

## Conclusion

A TimeLimiter is a powerful tool that ensures an application interacts with external services reliably and timely. It limits the amount of time the calling thread waits for a response, preventing slow or hanging services from impacting performance.

This prevents slow or hanging services from impacting the performance of the entire application and allows the application to return meaningful error messages or default responses to the user in a timely manner.