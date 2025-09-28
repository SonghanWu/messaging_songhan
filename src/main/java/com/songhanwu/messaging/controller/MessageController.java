package com.songhanwu.messaging.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.songhanwu.messaging.annotation.NeedAuth;
import com.songhanwu.messaging.dto.MessageDTO;
import com.songhanwu.messaging.enumeration.MessageType;
import com.songhanwu.messaging.request.SendMessageRequest;
import com.songhanwu.messaging.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@RestController
@RequestMapping("/messages")
@ConditionalOnProperty(name = "aws.enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class MessageController {

    @Autowired private MessageService messageService;
    @Autowired private S3Client s3Client;
    @Autowired private S3Presigner presigner;

    @PostMapping("/send")
    @NeedAuth
    public String sendMessage(@RequestBody SendMessageRequest sendMessageRequest) throws Exception {
        int messageId = this.messageService.sendMessage(sendMessageRequest.getGroupChatId(),
                                                        sendMessageRequest.getReceiverUserId(),
                                                        sendMessageRequest.getContent(),
                                                        sendMessageRequest.getMessageType());
        String content = null;
        if (sendMessageRequest.getMessageType() != MessageType.TEXT) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("jianjin-messaging-user-file")
                    .key(String.valueOf(messageId))
                    .build();
            PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                    .putObjectRequest(putObjectRequest)
                    .signatureDuration(Duration.ofMinutes(5))
                    .build();
            PresignedPutObjectRequest presignedPutObjectRequest =
                    this.presigner.presignPutObject(putObjectPresignRequest);
            content = presignedPutObjectRequest.url().toExternalForm();
        } else {
            content = sendMessageRequest.getContent();
        }

        this.messageService.sendPushNotificationTask(sendMessageRequest.getGroupChatId(),
                                                     sendMessageRequest.getReceiverUserId(),
                                                     content);

        return content;

    }

    @GetMapping("listMessages")
    @NeedAuth
    //change log
    public String listMessages(@RequestParam int lastReceivedMessageId) {
        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (MessageDTO messageDTO : messageDTOs) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("jianjin-messaging-user-file")
                    .key(String.valueOf(3))
                    .build();
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(5))
                    .build();
            PresignedGetObjectRequest presignedGetObjectRequest =
                    this.presigner.presignGetObject(getObjectPresignRequest);
            var url = presignedGetObjectRequest.url().toExternalForm();
        }
        return null;
    }

    @GetMapping("/testLongPolling") // GET /
    @NeedAuth
    public DeferredResult<Integer> testLongPolling() {

        log.info("Start testLongPolling");
        DeferredResult<Integer> deferredResult = new DeferredResult<>(10000L, -999);
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                deferredResult.setResult(15);
            } catch (Exception exception) {

            }
            return null;
        });
        log.info("About to finish testLongPolling");
        return deferredResult;

    }


}
