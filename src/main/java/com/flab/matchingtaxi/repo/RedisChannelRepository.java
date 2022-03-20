package com.flab.matchingtaxi.repo;

import com.flab.matchingtaxi.service.redis.RemoteMessageSubscriber;
import com.flab.matchingtaxi.std.RedisStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class RedisChannelRepository {

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private RemoteMessageSubscriber remoteMessageSubscriber;

    // redis
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, String> hashOperations;

    // topic hash table
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    // 토픽 생성, 구독
    public ChannelTopic createTopic(String name){
        try {
            if(!hashOperations.hasKey(RedisStandard.HASHOPS_KEY_CHANNEL, name)){
                hashOperations.put(RedisStandard.HASHOPS_KEY_CHANNEL, name, name);
            }
            String key = hashOperations.get(RedisStandard.HASHOPS_KEY_CHANNEL, name);
            ChannelTopic topic;
            if(topics.containsKey(key)){
                topic = topics.get(key);
            }
            else {
                topic = new ChannelTopic(key);
                topics.put(key, topic);
            }
            redisMessageListenerContainer.addMessageListener(remoteMessageSubscriber, topic);
            return topic;
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    // 토픽 제거
    public boolean deleteTopic(String name){
        ChannelTopic topic = topics.get(name);
        if(topic == null) return false;
        topics.remove(name, topic);
        redisMessageListenerContainer.removeMessageListener(remoteMessageSubscriber, topic);
        return true;
    }

    // 토픽 조회
    public ChannelTopic getTopic(String name){
        if(name != null) {
            String val = hashOperations.get(RedisStandard.HASHOPS_KEY_CHANNEL, name);
            return topics.get(val);
        }
        return null;
    }
}