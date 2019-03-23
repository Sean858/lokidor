package com.hackthon.demo.clzs;

public class PriorityLoc extends Loc{
    private double priority;
    private boolean show;

    public PriorityLoc(Loc loc, double priority, boolean show){
            super(loc);
            this.priority = priority;
            this.show = show;
            }

    public PriorityLoc(double lat, double lon, double priority, boolean show) {
            super(lat, lon);
            this.priority = priority;
            this.show = show;
            }
    public boolean isShow() {
            return show;
            }

    public void setShow(boolean show) {
            this.show = show;
            }

    public double getPriority() {
            return priority;
            }

    public void setPriority(double priority) {
            this.priority = priority;
            }
}
