package com.example.firebasetest;

public class GioInfo {
    private double Lat;
    private double Lon;

    public GioInfo(double lat, double lon) {
        Lat = lat;
        Lon = lon;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }
}
