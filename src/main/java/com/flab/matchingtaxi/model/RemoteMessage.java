package com.flab.matchingtaxi.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RemoteMessage{
    /**
     *  sender id
     */
    private String sender;

    /**
     *  receiver id
     */
    private String receiver;

    /**
     *  message contents
     */
    private Map<String, String> payload;
}