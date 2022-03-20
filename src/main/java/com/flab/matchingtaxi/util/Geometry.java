package com.flab.matchingtaxi.util;

import com.google.common.geometry.S2Point;

public class Geometry {
    public static S2Point pointFromLatLng(double lat, double lon){
        double x = Math.cos(lat) * Math.cos(lon);
        double y = Math.cos(lat) * Math.sin(lon);
        double z = Math.sin(lat);
        return new S2Point(x, y, z);
    }
}
