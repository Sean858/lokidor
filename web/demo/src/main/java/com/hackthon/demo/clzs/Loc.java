package com.hackthon.demo.clzs;

public class Loc {
    private double lat;
    private double lon;

    public Loc(){}

    public Loc(Loc loc){
        this.lat = loc.getLat();
        this.lon = loc.getLon();
    }

    public Loc(String location){
        String[] ll = location.split(",");
        this.lat = Double.parseDouble(ll[0]);
        this.lon = Double.parseDouble(ll[1]);
    }
    public Loc(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "{ lat:" + this.lat + ", lon:" + this.lon + "}";
    }
}
