package com.flab.matchingtaxi.controller;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.service.LocationService;
import com.flab.matchingtaxi.service.RemoteMessageService;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
@Slf4j
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private RemoteMessageService remoteMessageService;

    /**
     * req payload point, sender
     * @param message
     */
    @MessageMapping("/taxi/update/location")
    public void updateLocation(RemoteMessage message){
        try {
            String sender = remoteMessageService.extractSender(message);
            Point point = remoteMessageService.extractPoint(message);
            String cellId = locationService.updateLocation(sender, point);
            Map<String, Object> payload = new HashMap<>();
            payload.put(MessagePayloadStandard.UPDATE_CELLID, cellId);

            RemoteMessage response = RemoteMessage.builder()
                    .payload(payload)
                    .receiver(message.getSender())
                    .build();
            remoteMessageService.publish_stomp(response);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @MessageMapping("/taxi/testcase")
    public void testLocation(RemoteMessage message){
        try {
            double[][] points = {{37.673843, 126.786174}, {37.676867, 126.780251}, {37.682331, 126.779999}, {37.669452, 126.777314}, {37.659701, 126.783907}};
            for(int i=0; i<5; i++){
                Point point = new Point(points[i][0], points[i][1]);
                locationService.updateLocation(i+"", point);
            }
            log.info("test case input");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}