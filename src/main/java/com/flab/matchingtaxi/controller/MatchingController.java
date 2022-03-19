package com.flab.matchingtaxi.controller;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.service.MatchingService;
import com.flab.matchingtaxi.service.RedisChannelService;
import com.flab.matchingtaxi.service.RemoteMessageService;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private RemoteMessageService remoteMessageService;

    @Autowired
    private RedisChannelService redisChannelService;

    /**
     *  payload required key : state, sender
     */
    @MessageMapping("/taxi/update/state")
    public void updateMatchState(RemoteMessage message){
        try {
            boolean res = remoteMessageService.extractState(message);
            String topic = remoteMessageService.extractTopic(message);
            if(topic == null){
                topic = message.getSender();
            }
            if(res){
                redisChannelService.createTopic(topic);
                log.info("create topic");
            }
            else{
                redisChannelService.deleteTopic(topic);
                log.info("delete topic");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}