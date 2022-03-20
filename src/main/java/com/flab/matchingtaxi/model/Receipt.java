package com.flab.matchingtaxi.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Receipt {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String passengerId;

    @Column
    private String taxiId;

    @Column
    private int payLocation;

    @Column
    private int payAmount;

    @Column
    private int payUnit;

    @Column
    private double startLat;

    @Column
    private double startLng;

    @Column
    private double endLat;

    @Column
    private double endLng;

    @Column
    private Timestamp startTime;

    @Column
    private Timestamp endTime;

    @CreationTimestamp
    private Timestamp regTime;
}
