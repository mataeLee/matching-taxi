package com.flab.matchingtaxi.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.service.RedisChannelService;
import com.flab.matchingtaxi.service.RemoteMessageService;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
@Slf4j
public class RemoteMessageSubscriber implements MessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RemoteMessageService remoteMessageService;

    @Autowired
    private RedisChannelService redisChannelService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String val = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
            RemoteMessage remoteMessage = objectMapper.readValue(val, RemoteMessage.class);
            String type = remoteMessageService.extractType(remoteMessage);
            switch (type){
                case "send":
                    remoteMessageService.publish_stomp(remoteMessage);
                    break;
                case "unsubscribe":
                    InetAddress ip = InetAddress.getLocalHost();
                    String hostname = ip.getHostName();
                    if(!remoteMessageService.extractHostname(remoteMessage).equals(hostname)) {
                        String topic = remoteMessageService.extractTopic(remoteMessage);
                        redisChannelService.deleteTopic(topic);
                    }
                    remoteMessageService.publish_stomp(remoteMessage);
                    break;
                default:
                    log.error("message type null");
                    break;
            }
//            log.info("server -> user publish message from server");
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}