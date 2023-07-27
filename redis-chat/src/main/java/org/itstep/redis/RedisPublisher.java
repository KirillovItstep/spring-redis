package org.itstep.redis;

import org.itstep.domain.model.ChatMessage;
import org.itstep.domain.model.ChatMessagePayload;
import org.itstep.domain.model.ChatRoom;
import org.itstep.repository.ChatMessageRepository;
import org.itstep.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class RedisPublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    /**
     *  Description:
     *      - 메시지를 Redis Topic(채팅방 고유 아이디)에 발행(Publish)합니다.
     *
     */
    public void publish(ChannelTopic topic, ChatMessagePayload message) {
        ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoomId())
                .orElseGet(() -> ChatRoom.getBuilder()
                        .withName(UUID.randomUUID().toString())
                        .build());

        ChatMessage publishedMessage = ChatMessage.getBuilder()
                .withChatRoom(chatRoom)
                .withContent(message.getContent())
                .withSender(message.getSender())
                .withType(message.getMessageType())
                .build();

        chatRoom.addMessage(publishedMessage);
        chatMessageRepository.save(publishedMessage);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
