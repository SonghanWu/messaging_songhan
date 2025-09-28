package com.songhanwu.messaging.aspect;

import java.time.Instant;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

@Aspect
@Component
@ConditionalOnProperty(name = "aws.enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class LoggingAspect {

//    private static CloudWatchClient cloudWatchClient = CloudWatchClient.builder().build();
    @Autowired private CloudWatchClient cloudWatchClient;

    @Around("execution(* com.songhanwu.messaging.controller.*.*(..))")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        boolean errored = false;
        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception exception) {
            errored = true;
            throw exception;
        } finally {
            System.out.println("CloudWatch 上报执行了！");
            double latency = System.currentTimeMillis() - startTime;
            List<Dimension> dimensions = List.of(
                    Dimension.builder()
                            .name("Class")
                            .value(proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName())
                            .build(),
                    Dimension.builder()
                            .name("Method")
                            .value(proceedingJoinPoint.getSignature().getName())
                            .build());
            PutMetricDataRequest putMetricDataRequest = PutMetricDataRequest.builder()
                    .namespace("MessagingApplication")
                    .metricData(List.of(
                            MetricDatum.builder()
                                    .metricName("Invocation")
                                    .value(1.0)
                                    .unit(StandardUnit.COUNT)
                                    .timestamp(Instant.now())
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("Latency")
                                    .value(latency)
                                    .unit(StandardUnit.MILLISECONDS)
                                    .timestamp(Instant.now())
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("Error")
                                    .value(errored ? 1.0 : 0.0)
                                    .unit(StandardUnit.COUNT)
                                    .timestamp(Instant.now())
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("Invocation")
                                    .dimensions(dimensions)
                                    .value(1.0)
                                    .unit(StandardUnit.COUNT)
                                    .timestamp(Instant.now())
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("Latency")
                                    .dimensions(dimensions)
                                    .value(latency)
                                    .unit(StandardUnit.MILLISECONDS)
                                    .timestamp(Instant.now())
                                    .build(),
                            MetricDatum.builder()
                                    .metricName("Error")
                                    .dimensions(dimensions)
                                    .value(errored ? 1.0 : 0.0)
                                    .unit(StandardUnit.COUNT)
                                    .timestamp(Instant.now())
                                    .build()
                    ))
                    .build();
            cloudWatchClient.putMetricData(putMetricDataRequest); // error, invocation, latency
            log.info("{}.{} latency: {} ms errored: {}",
                     proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                     proceedingJoinPoint.getSignature().getName(),
                     latency, errored);
        }
    }

}
