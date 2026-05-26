package com.application.spotreview.model;

public class Spot {
    private int spotId;
    private String spotName;
    private double latitude;  // 위도
    private double longitude; // 경도
    private String address;

    public Spot(int spotId, String spotName, double latitude, double longitude, String address) {
        this.spotId = spotId;
        this.spotName = spotName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    // Getter / Setter
    public int getSpotId() { return spotId; }
    public void setSpotId(int spotId) { this.spotId = spotId; }

    public String getSpotName() { return spotName; }
    public void setSpotName(String spotName) { this.spotName = spotName; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
