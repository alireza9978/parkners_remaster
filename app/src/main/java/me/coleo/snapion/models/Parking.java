package me.coleo.snapion.models;

public class Parking {

    private int id;
    private String name;
    private String address;
    private float progress;
    private float distance;
    private float lat;
    private float lng;

    public Parking(String name, String address, float progress, float distance) {
        this.name = name;
        this.address = address;
        this.progress = progress;
        this.distance = distance;
    }

    public Parking(int id, String name, String address, float progress, float distance, float lat, float lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.progress = progress;
        this.distance = distance;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getDistanceString() {
        return distance + "KM";
    }
}
