## Circuit Breaker

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

To implement this pattern we use resilience4j. More information can be found at the link: https://resilience4j.readme.io/docs/circuitbreaker