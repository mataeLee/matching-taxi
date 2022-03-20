package com.flab.matchingtaxi.service;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.repo.RedisChannelRepository;
import com.flab.matchingtaxi.service.redis.RemoteMessagePublisher;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RedisChannelService {

    @Autowired
    private RedisChannelRepository redisChannelRepository;

    @Autowired
    private RemoteMessagePublisher remoteMessagePublisher;

    // 구독 생성
    public ChannelTopic createTopic(String topic){
        if(topic == null || topic.equals("")) return null;
        return redisChannelRepository.createTopic(topic);
    }

    // 구독 제거
    public boolean deleteTopic(String topic) {
        if (redisChannelRepository.deleteTopic(topic)) {
            log.info("delete topic successed");
            return true;
        }
        log.info("delete topic failed");
        return false;
    }

    // 발행
    public void publish(RemoteMessage message) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            String hostname = ip.getHostName();
            message.getPayload().put(MessagePayloadStandard.HOSTNAME, hostname);
            ChannelTopic channelTopic = getTopic((String)message.getPayload().get(MessagePayloadStandard.TOPIC));
            remoteMessagePublisher.publish_redis(channelTopic, message);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public ChannelTopic getTopic(String name) {
        return redisChannelRepository.getTopic(name);
    }

    public List<ChannelTopic> getNearestTopic(Point point) {
        //TODO extract nearest topic
        List<ChannelTopic> topics = new ArrayList<>();
        return topics;
    }
}