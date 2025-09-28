package com.songhanwu.messaging.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.songhanwu.messaging.context.UserContext;
import com.songhanwu.messaging.dao.MessageDAO;
import com.songhanwu.messaging.dto.MessageDTO;
import com.songhanwu.messaging.dto.PushNotificationTask;
import com.songhanwu.messaging.enumeration.MessageType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.GoneException;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ComparisonOperator;
import software.amazon.awssdk.services.dynamodb.model.Condition;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@ConditionalOnProperty(name = "aws.enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class MessageService {

    @Autowired GroupChatService groupChatService;
    @Autowired MessageDAO messageDAO;
    @Autowired DynamoDbClient dynamoDbClient;
    @Autowired ApiGatewayManagementApiClient apiGatewayManagementApiClient;
    @Autowired SqsClient sqsClient;
    @Autowired ObjectMapper objectMapper;

    public int sendMessage(Integer groupChatId,
                           Integer receiverUserId, // null
                           String content,
                           MessageType messageType) {

        if (groupChatId != null) {
            this.groupChatService.verifyUserIsAMember(UserContext.getCurrentUser().getId(), groupChatId);
        } else {
            // this.friendService.
        }

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSenderUserId(UserContext.getCurrentUser().getId());
        messageDTO.setGroupChatId(groupChatId);
        messageDTO.setReceiverUserId(receiverUserId);
        messageDTO.setContent(content);
        messageDTO.setMessageType(MessageType.TEXT);
        messageDTO.setSendTime(new Date());
        this.messageDAO.insert(messageDTO);
        return messageDTO.getId();

    }

    public void sendPushNotificationTask(Integer groupChatId,
                                         Integer receiverUserId,
                                         String content) throws Exception {
        List<Integer> userIdsToNotify = new ArrayList<>();
        if (receiverUserId != null) {
            userIdsToNotify.add(receiverUserId);
        } else {
            // add all group chat user ids to userIdsToNotify
        }

        for (int userIdToNotify : userIdsToNotify) {
            PushNotificationTask pushNotificationTask = new PushNotificationTask();
            pushNotificationTask.setUserIdToNotify(userIdToNotify);
            pushNotificationTask.setContent(content);

            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl("https://sqs.us-east-1.amazonaws.com/477193507016/Messages")
                    .messageBody(this.objectMapper.writeValueAsString(pushNotificationTask))
                    .build();
            this.sqsClient.sendMessage(sendMessageRequest);
        }

    }

    public void pushNotification(Integer groupChatId,
                                 Integer receiverUserId,
                                 String content) {
        List<Integer> userIdsToNotify = new ArrayList<>();
        if (receiverUserId != null) {
            userIdsToNotify.add(receiverUserId);
        } else {
            // add all group chat user ids to userIdsToNotify
        }

        for (var userIdToNotify : userIdsToNotify) {
            QueryRequest queryRequest = QueryRequest.builder()
                    .tableName("user-connections-songhan")
                    .keyConditions(Map.of(
                            "UserId", Condition.builder()
                                    .comparisonOperator(ComparisonOperator.EQ)
                                    .attributeValueList(AttributeValue.builder()
                                                                .s(String.valueOf(userIdToNotify))
                                                                .build())
                                    .build()
                    ))
                    .build();

            QueryResponse queryResponse = dynamoDbClient.query(queryRequest);
            for (var item : queryResponse.items()) {
                String connectionId = item.get("ConnectionId").s();
                String userId = item.get("UserId").s();
                log.info("Sending notification to user: {}, connection: {}", userId, connectionId);

                try {
                    PostToConnectionRequest postToConnectionRequest = PostToConnectionRequest.builder()
                            .connectionId(connectionId)
                            .data(SdkBytes.fromUtf8String(content))
                            .build();
                    this.apiGatewayManagementApiClient.postToConnection(postToConnectionRequest);
                } catch (GoneException goneException) {
                    log.info("Connection {} is gone. Deleting it from DynamoDB.", connectionId);
                    DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                            .tableName("user-connections-songhan")
                            .key(Map.of(
                                    "ConnectionId", AttributeValue.builder()
                                            .s(connectionId)
                                            .build(),
                                    "UserId", AttributeValue.builder()
                                            .s(userId)
                                            .build()
                            ))
                            .build();
                    this.dynamoDbClient.deleteItem(deleteItemRequest);
                }
            }
        }

    }
}
