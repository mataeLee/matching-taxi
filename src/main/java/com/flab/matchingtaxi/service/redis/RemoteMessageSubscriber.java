package com.flab.matchingtaxi.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.std.RedisStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoteMessageSubscriber implements MessageListener {

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String val = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
            RemoteMessage remoteMessage = mapper.readValue(val, RemoteMessage.class);
            switch (remoteMessage.getContent()){
                case RedisStandard.RM_COMMAND_DELETE:
                    boolean result = redisService.deleteTopic(remoteMessage.getTopic());
                    log.info("on message delete topic : " + result);
                    break;
                default:
                    messageSendingOperations.convertAndSend(RedisStandard.STOMP_DESTINATION_SUB + remoteMessage.getTopic(), remoteMessage);
                    log.info("on message : " + remoteMessage.toString());
                    break;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
