package com.hackthon.demo.helper;

public class TimeHelper {
    public static int timeToBucket(String s){
        int sH = Integer.parseInt(s.split(":")[0]), sM = Integer.parseInt(s.split(":")[1]);
        return sH * 60 + sM;
    }
}
