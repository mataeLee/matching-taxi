package com.flab.matchingtaxi.model;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RemoteMessage{
    private String sender;
    private String topic;
    private String content;
}
