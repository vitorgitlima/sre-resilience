## Circuit Breaker

## Overview
In this application, we used the Circuit Breaker Pattern. The idea behind it is to create a mechanism for when an error scenario begins, we stop forcing the external
service (open the circuit breaker) and then wait a time to call the resource again. The idea is that, when a service is not working properly, if we keep sending requests,
we can flood the service and cause more problems. A circuit breaker can have 3 states: closed, open and half-open.
* closed -> The initial state. This state allows the calls to happen as usual and monitor the number of failures occurring within the defined period. If the number of errors reaches the threshold,
  the state will change to an open state;
* open -> Once the circuit breaker moves to an open state, all the requests will be blocked. After a timeout period, the state will change to half-open;
* half-open -> The circuit breaker will allow a limited number of requests. If those requests are successful, the circuit breaker will switch to the closed state again.
  If not, it will block the requests again for a defined period of time.

The picture above can illustrate the 3 states and how it works:

![Alt text](./docs/circuit_breaker_states.jpg "Circuit Breaker states")

To implement this pattern we use resilience4j. More information can be found at [[Resilience4j Circuit Breaker Documentation]](https://resilience4j.readme.io/docs/circuitbreaker)

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
resilience4j.circuitbreaker:
  configs: # An optional property that allows defining pre-configurations for CircuitBreaker instances.
    default: # The name of the default pre-configuration being defined.
      registerHealthIndicator: true # A flag that indicates whether to register a health indicator for the CircuitBreaker.
      failure-rate-threshold: 50 # The failure rate threshold for the CircuitBreaker to transition to the "Open" state, expressed as a percentage.
      minimum-number-of-calls: 5  # The minimum number of calls required before the CircuitBreaker can calculate the error rate. If this value is set to 10, for example, at least 10 requests must be stored before the error rate can be calculated.
      automatic-transition-from-open-to-half-open-enabled: true # A flag that indicates whether to trigger a thread that transitions the CircuitBreaker from "Open" to "Half-Open" state when the wait duration expires. If set to false, the CircuitBreaker will wait for a new request to change state.
      wait-duration-in-open-state: 50s # The wait duration in the "Open" state. After this time has elapsed, the CircuitBreaker will transition to the "Half-Open" state.
      permitted-number-of-calls-in-half-open-state: 3 # The maximum number of requests allowed when in the "Half-Open" state. Any additional requests will be rejected with a CallNotPermittedException, until all permitted calls have been completed.
      sliding-window-size: 10 # The size of the window used to record the results of calls when the CircuitBreaker is closed (how many calls it remembers). This value must be greater than the analysis values. If it is smaller, it will overwrite the minimum required values.
      sliding-window-type: count_based # The type of sliding window used by the CircuitBreaker, either COUNT_BASED or TIME_BASED. If TIME_BASED, the seconds of the last requests will be stored for calculations.
  instances: # A property that allows defining CircuitBreaker instances.
    cosmoCircuitBreaker: # The name of the CircuitBreaker instance being defined.
      baseConfig: default # This configuration indicates that we are inheriting all default configurations. The following definitions override the default configuration and can be changed as needed.
      failure-rate-threshold: 90 # The failure rate threshold for the cosmoCircuitBreaker instance to transition to the "Open" state, expressed as a percentage. This value overrides the corresponding value in the default configuration.
      minimum-number-of-calls: 15 # The minimum number of calls required before the cosmoCircuitBreaker instance can calculate the error rate. This value overrides the corresponding value in the default configuration.
      sliding-window-size: 20 # The size of the window used to record the results of calls when the cosmoCircuitBreaker instance is closed (how many calls it remembers). This value overrides the corresponding value in the default configuration.
```

## Configuration

After the initial configuration, it is possible to create an interceptor for Circuit Breaker. In the example below, a log.info is triggered to register each Circuit request.

```java
    @Bean
    public RegistryEventConsumer<CircuitBreaker> myCircuitBreakerRegistryEventConsumer() {

        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher()
                        .onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
            }

        };
    }
```

## Usage

Here's an example of how to use Circuit Breaker in applications:

In this case, whenever the findById method or sub-method throws an exception, the error is recorded and counted in the `failure-rate-threshold property`. If the failure rate exceeds the configured value, the Circuit Breaker will transition to the Open state, where no requests will be accepted and a CallNotPermittedException will be thrown. Then, after the time set in the wait-duration-in-open-state property has elapsed, the Circuit Breaker will transition to the "Half-Open" state. In this state, a limited number of calls (configured by the `permitted-number-of-calls-in-half-open-state` property) will be allowed. If any of these calls still throws an exception, the Circuit Breaker will transition back to the "Open" state; otherwise, it will transition to the "Closed" state, allowing all requests to flow normally.

```java
    @CircuitBreaker(name = "cosmoCircuitBreaker")
    public Optional<DemoSRE> findById(final Integer id) {
        return repository.findById(id);
    }
```