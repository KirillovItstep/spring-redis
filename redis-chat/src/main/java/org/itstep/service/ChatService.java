package org.itstep.service;

import org.itstep.redis.RedisSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListener; // Listener(Subscriber)
    private Map<Long, ChannelTopic> topics;

    @Autowired
    public ChatService(RedisSubscriber redisSubscriber, RedisMessageListenerContainer redisMessageListener) {
        this.redisSubscriber = redisSubscriber;
        this.redisMessageListener = redisMessageListener;
        this.topics = new HashMap<>();
    }

    /**
     *  Description:
     *      - Redis 에 Topic 을 만들고 pub/sub 통신을 하기 위해 Listener 를 설정
     */
    public void enterChatRoom(Long id) {
        ChannelTopic topic = topics.get(id);
        if (topic == null) {
            topic = new ChannelTopic(id.toString());
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(id, topic);
        }
    }
}
