package com.hackthon.demo.helper;

import com.hackthon.demo.clzs.Loc;

public class DistanceHelper {
    public static double distance(Loc schLoc, Loc recLoc){
        double lat1 = schLoc.getLat(), lon1 = schLoc.getLon(), lat2 = recLoc.getLat(), lon2 = recLoc.getLon();
        final double R = 6371.01;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }
}
