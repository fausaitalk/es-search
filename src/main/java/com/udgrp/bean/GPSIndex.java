
package com.udgrp.bean;

import java.io.Serializable;

/**
 * Created by kejw on 2017/8/16.
 */
public class GPSIndex implements Serializable {

    private String plate;
    private String vehicleColor;
    private String vehicleType;
    private String date;
    private String rowKey;
    private String enCity;
    private String exCity;
    private String isSameCity;

    public String getEnCity() {
        return enCity;
    }

    public void setEnCity(String enCity) {
        this.enCity = enCity;
    }

    public String getExCity() {
        return exCity;
    }

    public void setExCity(String exCity) {
        this.exCity = exCity;
    }

    public String getIsSameCity() {
        return isSameCity;
    }

    public void setIsSameCity(String isSameCity) {
        this.isSameCity = isSameCity;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
