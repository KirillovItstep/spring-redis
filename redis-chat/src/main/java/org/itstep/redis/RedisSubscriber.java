package org.itstep.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.itstep.domain.model.ChatMessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public RedisSubscriber(ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate, SimpMessageSendingOperations messagingTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     *  Description:
     *      - Redis 에서 메시지가 발행(Publish)되면 대기하고 있던 onMessage()가 해당 메시지를 받아 처리합니다(Subscribe)
     *
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis Deserialize
            String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());

            // ChatMessage
            ChatMessagePayload chatMessage = objectMapper.readValue(publishedMessage, ChatMessagePayload.class);

            // WebSocket
            messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatMessage.getChatRoomId().toString(), chatMessage);
        }
        catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
