package com.flab.matchingtaxi.service;

import com.flab.matchingtaxi.domain.CallRequest;
import com.flab.matchingtaxi.model.Receipt;
import com.flab.matchingtaxi.model.RemoteMessage;
import com.flab.matchingtaxi.repo.LocationRepository;
import com.flab.matchingtaxi.repo.ReceiptRepository;
import com.flab.matchingtaxi.std.MessagePayloadStandard;
import com.flab.matchingtaxi.util.Geometry;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.geometry.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class MatchingService {
    private final int base_cell_level = 14;

    @Autowired
    private RedisTemplate<String , String> redisTemplate;

    @PostConstruct
    private void init(){
    }

    /**
     * @return nearest taxi receiver message for publish
     */
    public Set<S2CellId> getNearbyCellIdsByStep(CallRequest request){
        Point obj = request.getStart();
        S2CellId target_cell = S2CellId.fromLatLng(S2LatLng.fromDegrees(obj.getX(), obj.getY())).parent(base_cell_level);

        int step = request.getCount();
        if(step > 5) return null;

        List<S2CellId> cellIds = new ArrayList<>();
        cellIds.add(target_cell);
        target_cell.getAllNeighbors(base_cell_level, cellIds);

        Set<S2CellId> cellIdSet = Sets.newHashSet(cellIds);
        if(step != 1){
            Set<S2CellId> cellIds_before = new HashSet<>();
            for(int i=0; i<step; i++){
                Iterator<S2CellId> it = cellIdSet.iterator();
                while (it.hasNext()){
                    S2CellId cell = it.next();
                    cell.getAllNeighbors(base_cell_level, cellIds);
                }
                cellIdSet = Sets.newHashSet(cellIds);
                if(i == step-1) cellIds_before.addAll(cellIdSet);
            }
            Iterator<S2CellId> it = cellIds_before.iterator();
            while (it.hasNext()){
                cellIdSet.remove(it.next());
            }
        }
        return cellIdSet;
    }

    // cell 갯수 (2n-1)^2으로 증가 최대 5 times
    public Set<S2CellId> getNearbyCellIdsMore(Set<S2CellId> cellIds) {
        Iterator<S2CellId> iterator = cellIds.iterator();
        List<S2CellId> res = Lists.newArrayList(cellIds);
        while (iterator.hasNext()){
            S2CellId target = iterator.next();
            target.getAllNeighbors(base_cell_level, res);
        }
        return Sets.newHashSet(res);
    }
}