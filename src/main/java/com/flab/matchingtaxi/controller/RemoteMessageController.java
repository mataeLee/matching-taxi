package com.flab.matchingtaxi.controller;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.service.RedisChannelService;
import com.flab.matchingtaxi.service.RemoteMessageService;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class RemoteMessageController {

    @Autowired
    private RedisChannelService redisChannelService;

    @Autowired
    private RemoteMessageService remoteMessageService;

    /**
     *  payload required key : sender, receiver, topic
     */
    @MessageMapping("/message")
    public void publishMessage(RemoteMessage message){
        try {
            // 유저 -> 서버(다른 유저에게) 메시징
            message = remoteMessageService.putType(message, MessagePayloadStandard.SEND);
            redisChannelService.publish(message);
            log.info("user->server messaging");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}