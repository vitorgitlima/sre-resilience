# Bulkhead Patters

## Overview
The Bulkhead Pattern is a concurrency pattern used to improve the reliability and fault tolerance of applications by isolating different parts of the application into separate "bulkheads." In a bulkhead architecture, each part of the application is divided into separate partitions, and each partition has its own resources and threads.

The Bulkhead Pattern is typically used to prevent failures in one area of the application from cascading to other areas. For example, if one thread or task fails in a particular section of code, the bulkhead architecture can prevent that failure from affecting other parts of the application.

## Benefits

* Improved resilience: Allow you to preserve some functionality in case of service failure. Other app services and features will continue to function.

* Better resource management: By assigning resources such as threads or connections to specific components, resource usage can be better managed and optimized.

* Improve performance by preventing threads or tasks from interfering with each other. By limiting the number of threads or tasks that can access a particular resource or section of code, you can prevent contention and improve the overall performance of the system.

![Alt text](./docs/bulkhead_example.png "Bulkhead pattern")

To implement this pattern, we use resilience4j. More information can be found at [Resilience4j Retry Documentation](https://resilience4j.readme.io/docs/bulkhead) :link:

## Implementation

There are two implementations of bulkhead patterns in Resilience4j.

* ### *Semaphore -* In this approach, we limit the number of concurrent requests to the service. It will reject the incoming requests once the limit is hit.

* ### *ThreadPoolBulkhead -* In this approach, we limit the number of concurrent requests to the service. It will reject the incoming requests once the limit is hit.

## How to set Bulkhead in your application

First we need to add a new dependency on pom.xml

```xml
        <!-- BulkHead -->
        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-bulkhead</artifactId>
            <version>1.7.1</version>
        </dependency>

        <dependency>
            <groupId>io.github.resilience4j</groupId>
            <artifactId>resilience4j-spring-boot2</artifactId>
            <version>1.7.1</version> <!-- For best results, ensure that your application is always running on the most recent LTS version it can handle -->
        </dependency>
```

Then add a new configuration in application.yml

```yaml
resilience4j.bulkhead: # This line sets the root key for defining a Bulkhead configuration with Resilience4j library.
  instances: # Defining Bulkhead instances.
    semaphoreBulkhead: # Declares a specific instance of a Bulkhead with the name "semaphoreBulkhead".
      maxConcurrentCalls: 2 # These two lines set the maximum number of concurrent calls allowed for the "semaphoreBulkhead" Bulkhead instance to 2 and the maximum wait duration for acquiring the Bulkhead lock to 1 second.
      maxWaitDuration: 1s

resilience4j: # This line sets the root key for defining a Thread Pool Bulkhead configuration with Resilience4j library.
  thread-pool-bulkhead: # This section declares a default Thread Pool Bulkhead configuration with the following parameters:
    configs:
      default:
        maxThreadPoolSize: 1 # The maximum number of threads in the thread pool.
        coreThreadPoolSize: 1 # The core number of threads in the thread pool.
        queueCapacity: 1 # The maximum number of requests that can be queued when all threads in the thread pool are busy.
        keepAliveDuration: 20 # The time duration after which an idle thread in the pool is terminated.
    instances: # 
      bulkheadInstance: # Declares a specific instance of the thread Pool Bulkhead with the name "bulkheadInstance".
        baseConfig: default
        maxThreadPoolSize: 1
        coreThreadPoolSize: 1
        queueCapacity: 1
        keepAliveDuration: 20
```

## Configuration

After the initial configuration, it is possible to create an interceptor for Retry. In the example below, a log.info is triggered to register each Retry attempt, and if a property named `sre.resilience.retry.throw-sre-max-retries-exceed` is set to TRUE, a custom exception is thrown.

```java
    @Bean
    public RegistryEventConsumer<Retry> myRetryRegistryEventConsumer(@Value("${sre.resilience.retry.throw-sre-max-retries-exceed:false}") boolean useSRERetryException) {

        return new RegistryEventConsumer<Retry>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher()
                        .onEvent(event -> {
                            int maxAttemps = entryAddedEvent.getAddedEntry().getRetryConfig().getMaxAttempts();
                            String logMessage = MessageFormat.format("[RETRY][{2}/{3}] Time: {0}, Name: {1}, Exception: {4}",
                                    event.getCreationTime(), event.getName(), event.getNumberOfRetryAttempts(), maxAttemps, event.getLastThrowable());
                            log.info(logMessage);

                            if (useSRERetryException && event.getNumberOfRetryAttempts() >= maxAttemps) {
                                throw new DemoSREMaxRetriesExceededException();
                            }
                        });
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
            }
        };
    }

```

## Usage

Here's an example of how to use Bulkhead in applications:

In this case, whenever the `findById` method or sub-method throws an exception that extends `RetriableBaseException` or is included in the `retryExceptions` property, a new attempt will be executed until the maximum limit is reached (in this case, 5 attempts)

```java
    @Retry(name = "cosmoRetry")
    public Optional<DemoSRE> findById(final Integer id) {
        return repository.findById(id);
    }
```