 

package com.udgrp.bean;

import java.io.Serializable;

/**
 * Created by kejw on 2017/8/21.
 */
public class GPSRecordInfo implements Serializable {
    private String plate       ;
    private String vehicleColor;
    private String vehicleType ;
    private String GPSTime     ;
    private String lon         ;
    private String lat         ;
    private String vec1        ;
    private String vec2        ;
    private String vec3        ;
    private String direction   ;
    private String altitude    ;
    private String state       ;
    private String alarm       ;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getGPSTime() {
        return GPSTime;
    }

    public void setGPSTime(String GPSTime) {
        this.GPSTime = GPSTime;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getVec1() {
        return vec1;
    }

    public void setVec1(String vec1) {
        this.vec1 = vec1;
    }

    public String getVec2() {
        return vec2;
    }

    public void setVec2(String vec2) {
        this.vec2 = vec2;
    }

    public String getVec3() {
        return vec3;
    }

    public void setVec3(String vec3) {
        this.vec3 = vec3;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}
