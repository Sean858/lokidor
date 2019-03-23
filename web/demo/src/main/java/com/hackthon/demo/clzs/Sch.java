package com.hackthon.demo.clzs;

public class Sch {
    private String start;
    private String stop;
    private Loc location;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public Loc getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = new Loc(location);
    }
}
