package com.flab.matchingtaxi.service;

import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.repo.LocationRepository;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import com.flab.matchingtaxi.util.Geometry;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class LocationService {
    private final int base_cell_level = 14;

    @Autowired
    private LocationRepository locationRepository;

    @PostConstruct
    public void init(){

    }

    public String updateLocation(String sender, Point point) {
        S2CellId cellId = S2CellId.fromLatLng(S2LatLng.fromDegrees(point.getX(), point.getY())).parent(base_cell_level);
        return locationRepository.updateLocation(sender, cellId.id()+"");
    }

    public Set<Object> getTaxisFromCellIds(Set<S2CellId> cellIds) {
        Set<Object> taxiList = locationRepository.getTaxiListFromCellIds(cellIds);
        return taxiList;
    }

    public void removeLocation(RemoteMessage message) {
        String cellid = (String)message.getPayload().get(MessagePayloadStandard.CELLID);
        locationRepository.removeLocation(message.getSender(), cellid);
    }

//
//    public void removeLocation(String key, String sender){
//        locationRepository.removeLocation(key, sender);
//    }
//
//    public Point getLocation(String key, String sender){
//        return locationRepository.getLocation(key, sender);
//    }
//
//    public void getLocationByRadius(Point point, int radius){
//        GeoResults<RedisGeoCommands.GeoLocation<Object>> res =  locationRepository.getLocationsByRadius(point, radius);
//
//    }
}