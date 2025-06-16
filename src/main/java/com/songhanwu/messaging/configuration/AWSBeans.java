package com.songhanwu.messaging.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

@Configuration
public class AWSBeans {

    @Bean
    public CloudWatchClient cloudWatchClient(
            @Value("${aws.region:#{systemEnvironment['AWS_REGION'] ?: 'us-east-1'}}") String region) {
        System.out.println("CloudWatch region: " + region);
        return CloudWatchClient.builder().region(Region.of(region)).build();
    }
}
