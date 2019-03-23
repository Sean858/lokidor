package com.hackthon.demo.clzs;


public class SensitiveLoc extends Loc{
    private int sensitivity;    // by happened time
    public SensitiveLoc(Loc loc, int sensitivity){
        super(loc);
        this.sensitivity = sensitivity;
    }

    public SensitiveLoc(double lat, double lon, int sensitivity){
        super(lat, lon);
        this.sensitivity = sensitivity;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

}
