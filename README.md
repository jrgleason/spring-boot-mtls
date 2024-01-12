# spring-boot-mtls
This is an example of a Spring Boot Server and a Spring Batch Job communicating using mTLS

## Prepare

1. Go into the server project and rung `./gen-test-store.sh`
2. Go into the client project and rung `./gen-client-cert.sh`

## Run the server

1. Go into the server project and run `./gradlew bootRun`

## Run the client

1. Go into the client project and run `./gradlew bootRun`

## Expected output

The client should print the following:

```
2024-01-11T23:24:52.560-05:00  INFO 98632 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=my-job]] launched with the following parameters: [{'JobID':'{value=1705033492516, type=class java.lang.String, identifying=true}'}]
Before Job
2024-01-11T23:24:52.595-05:00  INFO 98632 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [my-step]
OK
2024-01-11T23:24:53.398-05:00  INFO 98632 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [my-step] executed in 802ms
After Job
2024-01-11T23:24:53.403-05:00  INFO 98632 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=my-job]] completed with the following parameters: [{'JobID':'{value=1705033492516, type=class java.lang.String, identifying=true}'}] and the following status: [COMPLETED] in 830ms
```