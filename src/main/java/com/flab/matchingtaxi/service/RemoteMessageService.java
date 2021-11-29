package com.flab.matchingtaxi.service;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import com.flab.matchingtaxi.std.StompStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class RemoteMessageService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    // 발행
    public void publish_stomp(RemoteMessage message) {
        String receiverId = message.getReceiver();
        log.info("publish message : " + message.getPayload() + ", sender : " + message.getSender() + ", receiver : " + message.getReceiver());
        log.info("message destination : " + StompStandard.PUBLISH_PREFIX + receiverId);
        messagingTemplate.convertAndSend(StompStandard.PUBLISH_PREFIX + receiverId, message);
    }

    // extract
    public Boolean extractState(RemoteMessage message) {
        Map<String, String> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.STATE)) return null;
        return Boolean.parseBoolean(map.get(MessagePayloadStandard.STATE));
    }

    public String extractTopic(RemoteMessage message){
        Map<String, String> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.TOPIC)) return null;
        return map.get(MessagePayloadStandard.TOPIC);
    }

    public Boolean extractResponse(RemoteMessage message) {
        Map<String, String> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.RESPONSE)) return null;
        return Boolean.parseBoolean(map.get(MessagePayloadStandard.RESPONSE));
    }

    public Point extractPoint(RemoteMessage message) {
        Map<String, String> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.POINT_X) || !map.containsKey(MessagePayloadStandard.POINT_Y)) return null;
        double x = Double.parseDouble(map.get(MessagePayloadStandard.POINT_X));
        double y = Double.parseDouble(map.get(MessagePayloadStandard.POINT_Y));
        Point point = new Point(x, y);
        return point;
    }

    public String extractType(RemoteMessage message) {
        Map<String, String> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.TYPE)) return null;
        return map.get(MessagePayloadStandard.TYPE);
    }

    public RemoteMessage putType(RemoteMessage message, String value) {
        Map<String, String> map = message.getPayload();
        map.put(MessagePayloadStandard.TYPE, value);
        return message;
    }

    public String extractHostname(RemoteMessage message) {
        Map<String, String> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.HOSTNAME)) return null;
        return map.get(MessagePayloadStandard.HOSTNAME);
    }
}