package com.flab.matchingtaxi.controller;

import com.flab.matchingtaxi.domain.CallRequest;
import com.flab.matchingtaxi.model.Receipt;
import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.service.*;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import com.google.common.geometry.S2CellId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
@Slf4j
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RemoteMessageService remoteMessageService;

    @Autowired
    private RedisChannelService redisChannelService;

    @Autowired
    private ReceiptService receiptService;

    /**
     *  payload required key : state, sender, cellid
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
                locationService.removeLocation(message);
                log.info("delete topic");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *  payload required key : call req
     */
    @MessageMapping("/passenger/call")
    public void callTaxi(RemoteMessage message){
        try {
            CallRequest request = remoteMessageService.extractCallRequest(message);

            // get taxi list
            Set<S2CellId> cellIds = matchingService.getNearbyCellIdsByStep(request);

            if(cellIds == null){
                Map<String, Object> payload = new HashMap<>();
                payload.put(MessagePayloadStandard.CALL_RESULT, MessagePayloadStandard.ERROR_COUNT_VAL);
                RemoteMessage response = RemoteMessage.builder()
                        .receiver(message.getReceiver())
                        .payload(payload)
                        .build();
                remoteMessageService.publish_stomp(response);
                return;
            }

            Set<Object> taxiIds = locationService.getTaxisFromCellIds(cellIds);

            // 반경에 택시를 못찾는 경우
            if(taxiIds.size() == 0) {
                Map<String, Object> payload = new HashMap<>();
                payload.put(MessagePayloadStandard.CALL_RESULT, MessagePayloadStandard.NOT_FOUND_TAXI);
                RemoteMessage response = RemoteMessage.builder()
                        .receiver(message.getReceiver())
                        .payload(payload)
                        .build();
                remoteMessageService.publish_stomp(response);
                return;
            }

            // create match receipt
            receiptService.saveReceiptByCallRequest(request);

            // max taxi 동시 요청 숫자 제한
            // publish request
            Map<String, Object> payload = new HashMap<>();
            payload.put(MessagePayloadStandard.CALL_REQUEST, MessagePayloadStandard.TRUE);
            payload.put(MessagePayloadStandard.START_LAT, request.getStart().getX());
            payload.put(MessagePayloadStandard.START_LNG, request.getStart().getY());
            payload.put(MessagePayloadStandard.END_LAT, request.getEnd().getX());
            payload.put(MessagePayloadStandard.END_LNG, request.getEnd().getY());

            Iterator<Object> it = taxiIds.iterator();
            int req_count = 0;
            while (it.hasNext() && req_count < 21) {
                RemoteMessage publish_request = RemoteMessage.builder()
                        .sender(message.getSender())
                        .receiver((String)it.next())
                        .payload(payload)
                        .build();
                remoteMessageService.publish_stomp(publish_request);
                req_count++;
            }

            payload = new HashMap<>();
            payload.put(MessagePayloadStandard.CALL_RESULT, MessagePayloadStandard.TAXI_COUNT + ":" + req_count);
            RemoteMessage response = RemoteMessage.builder()
                    .receiver(message.getReceiver())
                    .payload(payload)
                    .build();
            remoteMessageService.publish_stomp(response);

        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *  payload required key : response, receiver
     */
    @MessageMapping("/taxi/response/call")
    public void responseCall(RemoteMessage message){
        try {
            boolean res = remoteMessageService.extractCallResponse(message);
            Receipt receipt = receiptService.findReceiptByPassengerId(message.getReceiver());
            if(res && receipt != null && receipt.getTaxiId() == null){
                receipt.setTaxiId(message.getSender());
                receiptService.saveReceipt(receipt);
            }else {
                Map<String, Object> payload = new HashMap<>();
                payload.put(MessagePayloadStandard.CALL_RESULT, MessagePayloadStandard.MATCH_FAIL);
                RemoteMessage response = RemoteMessage.builder()
                        .receiver(message.getSender())
                        .payload(payload)
                        .build();
                remoteMessageService.publish_stomp(response);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}