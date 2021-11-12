package com.flab.matchingtaxi.util;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.std.RedisStandard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RemoteMessageMapper {

    public static RemoteMessage paramMapping(String sender, String topic, String content){
        return RemoteMessage.builder()
                .sender(sender)
                .topic(topic)
                .content(content)
                .build();
    }
}
