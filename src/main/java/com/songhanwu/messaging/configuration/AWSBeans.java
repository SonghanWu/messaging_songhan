package com.songhanwu.messaging.configuration;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@ConditionalOnProperty(name = "aws.enabled", havingValue = "true", matchIfMissing = true)
public class AWSBeans {

    @Bean
    public CloudWatchClient cloudWatchClient(
            @Value("${aws.region:#{systemEnvironment['AWS_REGION'] ?: 'us-east-1'}}") String region) {
        System.out.println("CloudWatch region: " + region);
        return CloudWatchClient.builder().region(Region.of(region))
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder().s3Client(this.s3Client()).build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder().build();
    }

    @Bean
    public ApiGatewayManagementApiClient apiGatewayManagementApiClient() {
        return ApiGatewayManagementApiClient.builder()
                .region(Region.of("us-east-1"))
                .endpointOverride(URI.create("https://r9ka6r29e3.execute-api.us-east-1.amazonaws.com/production/"))
                .build();
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
