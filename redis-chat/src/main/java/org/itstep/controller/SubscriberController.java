package org.itstep.controller;

import lombok.extern.slf4j.Slf4j;
import org.itstep.domain.model.ChatMessage;
import org.itstep.domain.model.ChatMessagePayload;
import org.itstep.domain.model.ChatRoom;
import org.itstep.redis.RedisSubscriber;
import org.itstep.repository.ChatMessageRepository;
import org.itstep.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/chat")
public class SubscriberController {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private RedisSubscriber redisSubscriber;

    @Autowired
    private RedisMessageListenerContainer redisMessageListener;  // (Topic) Listener(Subscriber)

    /**
     *  Description:
     *      - 채팅방 이름을 요청 파라미터로 전달해서 채팅방 개설
     *
     */
    @PostMapping
    public ChatRoom createChatRoom(@RequestParam String name) {
        ChatRoom newChatRoom = ChatRoom.getBuilder()
                .withName(name)
                .build();
        newChatRoom = chatRoomRepository.save(newChatRoom);
        ChannelTopic topic = ChannelTopic.of(newChatRoom.getId().toString());
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        return chatRoomRepository.save(newChatRoom);
    }

    /**
     *  Description:
     *      - 전체 채팅방 목록을 가져옵니다
     *
     */
    @GetMapping
    public List<ChatRoom> getChatRooms() {
        return chatRoomRepository.findAll();
    }

    /**
     *  Description:
     *      - 해당 아이디를 가진 채팅방의 채팅을 가져옵니다
     *      - TODO: 페이징 추가
     *
     */
    @GetMapping(path = "/rooms/{chatRoomId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable("chatRoomId") Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);

        if (chatRoom != null) {
            return new ResponseEntity<>(chatRoom.getMessages(), HttpStatus.OK);
        }
        log.error("Error message - chat room not found with id: {}", chatRoomId);
        return new ResponseEntity<>("Error message - chat room not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/rooms/{chatRoomId}")
    public ChatMessage postChatMessages(@RequestBody ChatMessagePayload message) {
        System.out.println(message.getChatRoomId());
        ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoomId()).orElse(null);
        ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withChatRoom(chatRoom)
                .withContent(message.getContent())
                .withSender(message.getSender())
                .withType(message.getMessageType())
                .build();
            return chatMessageRepository.save(chatMessage);
    }
}
