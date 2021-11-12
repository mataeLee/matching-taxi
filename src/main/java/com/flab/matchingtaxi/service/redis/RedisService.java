package com.flab.matchingtaxi.service.redis;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.repo.redis.RemoteMessageRepository;
import com.flab.matchingtaxi.service.redis.RemoteMessagePublisher;
import com.flab.matchingtaxi.std.RedisStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RemoteMessageRepository remoteMessageRepository;

    @Autowired
    private RemoteMessagePublisher remoteMessagePublisher;

    // 구독 생성
    public ChannelTopic createTopic(String name){
        return remoteMessageRepository.createTopic(name);
    }

    // 구독 제거
    public boolean deleteTopic(String name) {
        if (getTopic(name) != null) {
            RemoteMessage message = RemoteMessage.builder()
                    .topic(name)
                    .content(RedisStandard.RM_COMMAND_DELETE)
                    .build();
            publish(message);
        }
        if (remoteMessageRepository.deleteTopic(name)) {
            log.info("delete topic successed");
            return true;
        }
        log.info("delete topic failed");
        return false;
    }

    // 발행
    public void publish(RemoteMessage message) {
        ChannelTopic topic = getTopic(message.getTopic());
        if(topic != null) {
            remoteMessagePublisher.publish(topic, message);
        }
    }

    public ChannelTopic getTopic(String name) {
        return remoteMessageRepository.getTopic(name);
    }
}