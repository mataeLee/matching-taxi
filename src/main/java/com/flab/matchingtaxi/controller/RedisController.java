package com.flab.matchingtaxi.controller;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.service.redis.RedisService;
import com.flab.matchingtaxi.util.RemoteMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/publish")
    private ResponseEntity publish(@RequestParam String topic, @RequestParam String content){
        RemoteMessage message = RemoteMessageMapper.paramMapping("", topic, content);
        redisService.publish(message);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/post/topic")
    private ResponseEntity createTopic(@RequestParam String name){
        ChannelTopic topic = redisService.createTopic(name);
        return new ResponseEntity(topic, HttpStatus.OK);
    }

    @DeleteMapping("/delete/topic")
    private ResponseEntity deleteTopic(@RequestParam String name){
        redisService.deleteTopic(name);
        return new ResponseEntity(HttpStatus.OK);
    }
}