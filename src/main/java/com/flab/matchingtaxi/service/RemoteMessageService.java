package com.flab.matchingtaxi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flab.matchingtaxi.domain.CallRequest;
import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import com.flab.matchingtaxi.std.StompStandard;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
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
        Map<String, Object> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.STATE)) return null;
        return (Boolean)map.get(MessagePayloadStandard.STATE);
    }

    public String extractTopic(RemoteMessage message){
        Map<String, Object> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.TOPIC)) return null;
        return (String)map.get(MessagePayloadStandard.TOPIC);
    }

    public Boolean extractCallResponse(RemoteMessage message) {
        Map<String, Object> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.CALL_RESPONSE)) return null;
        return (Boolean)map.get(MessagePayloadStandard.CALL_RESPONSE);
    }

    public Point extractPoint(RemoteMessage message) {
        Map<String, Object> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.LATITUDE) || !map.containsKey(MessagePayloadStandard.LONGITUDE)) return null;
        double x = (Double)map.get(MessagePayloadStandard.LATITUDE);
        double y = (Double)map.get(MessagePayloadStandard.LONGITUDE);
        Point point = new Point(x, y);
        return point;
    }

    public String extractType(RemoteMessage message) {
        Map<String, Object> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.TYPE)) return null;
        return (String)map.get(MessagePayloadStandard.TYPE);
    }

    public RemoteMessage putType(RemoteMessage message, String value) {
        Map<String, Object> map = message.getPayload();
        map.put(MessagePayloadStandard.TYPE, value);
        return message;
    }

    public String extractHostname(RemoteMessage message) {
        Map<String, Object> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.HOSTNAME)) return null;
        return (String)map.get(MessagePayloadStandard.HOSTNAME);
    }

    public String extractSender(RemoteMessage message) {
        return message.getSender();
    }

    public String extractReceiver(RemoteMessage message) {
        return message.getReceiver();
    }

    public Integer extractRadius(RemoteMessage message) {
        Map<String, Object> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.RADIUS)) return null;
        return (Integer)map.get(MessagePayloadStandard.RADIUS);
    }

    public CallRequest extractCallRequest(RemoteMessage message) {
        Map<String, Object> map = message.getPayload();
        if(!map.containsKey(MessagePayloadStandard.START_LAT) || !map.containsKey(MessagePayloadStandard.START_LNG)
                || !map.containsKey(MessagePayloadStandard.END_LAT)|| !map.containsKey(MessagePayloadStandard.END_LNG)) return null;
        CallRequest val = new CallRequest();
        val.setPassenger((String)map.get(MessagePayloadStandard.SENDER));
        val.setStart(new Point((double)map.get(MessagePayloadStandard.START_LAT), (double)map.get(MessagePayloadStandard.START_LNG)));
        val.setEnd(new Point((double)map.get(MessagePayloadStandard.END_LAT), (double)map.get(MessagePayloadStandard.END_LNG)));
        return val;
    }
}