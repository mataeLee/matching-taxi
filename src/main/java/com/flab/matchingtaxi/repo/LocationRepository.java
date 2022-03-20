package com.flab.matchingtaxi.repo;

import com.flab.matchingtaxi.std.RedisStandard;
import com.google.common.geometry.S2CellId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
@Slf4j
public class LocationRepository {
    // redis
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    private GeoOperations<String, Object> geoOperations;

    // s2 cell id, taxi ids
    private ListOperations<String, Object> listOperations;

    // taxi location <channel, id, s2 cellid>
    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    private void init()
    {
//        geoOperations = redisTemplate.opsForGeo();
        listOperations = redisTemplate.opsForList();
        hashOperations = redisTemplate.opsForHash();
    }

    public String updateLocation(String sender, String cellId){
        String val = hashOperations.get(RedisStandard.TAXI_LOCATION, sender);
        log.info("val is : " + val + ", cellid is : " + cellId);

        if(cellId.equals(val)) {
            return val;
        }

        if(val != null) {
            List<Object> res = listOperations.range(val, 0, listOperations.size(val));
            for (Object obj : res) {
                System.out.println(obj);
                if (((String) obj).equals(sender)) {
                    listOperations.remove(val, 1, obj);
                    break;
                }
            }
        }
        listOperations.leftPush(cellId, sender);
        hashOperations.put(RedisStandard.TAXI_LOCATION, sender, cellId);
        return cellId;
    }

    public Set<Object> getTaxiListFromCellIds(Set<S2CellId> cellIds) {
        Set<Object> taxiList = new HashSet<>();
        if(cellIds.size() == 0) return taxiList;

        Iterator<S2CellId> iterator = cellIds.iterator();
        while (iterator.hasNext()){
            String cellId = iterator.next().id()+"";
            List<Object> list = listOperations.range(cellId, 0, listOperations.size(cellId));
            taxiList.addAll(list);
        }
        return taxiList;
    }

    public void removeLocation(String sender, String cellid) {
        listOperations.remove(cellid, 1, sender);
        hashOperations.delete(RedisStandard.TAXI_LOCATION, sender);
    }
    /**
     *  redis geo ops func
     */
/*    public void updateLocation(String sender, Point point){
        geoOperations.add(RedisStandard.GEOOPS_KEY_TAXI_LOCATION, point, sender);
    }

    public void removeLocation(String key, String sender){
        geoOperations.remove(RedisStandard.GEOOPS_KEY_TAXI_LOCATION, sender);
    }

    public Point getLocation(String key, String sender){
        List<Point> res =  geoOperations.position(RedisStandard.GEOOPS_KEY_TAXI_LOCATION, sender);
        if(res.size() == 0) return null;
        return res.get(0);
    }

    public GeoResults<RedisGeoCommands.GeoLocation<Object>> getLocationsByRadius(Point point, int radius){
        Circle circle = new Circle(point, radius);
        return  geoOperations.radius(RedisStandard.GEOOPS_KEY_TAXI_LOCATION, circle);
    }*/
}