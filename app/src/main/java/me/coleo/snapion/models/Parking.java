package me.coleo.snapion.models;

import java.util.ArrayList;

public class Parking {

    private int id;
    private String title;
    private String address_text;
    private float total_capacity;
    private float address_latitude;
    private float address_longitude;
    private ArrayList<Details> payment_texts;
    private ArrayList<Details> working_detail_texts;
    private ArrayList<Image> images;

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", address_text='" + address_text + '\'' +
                ", total_capacity=" + total_capacity +
                ", address_latitude=" + address_latitude +
                ", address_longitude=" + address_longitude +
                '}';
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public float getAddress_latitude() {
        return address_latitude;
    }

    public void setAddress_latitude(float address_latitude) {
        this.address_latitude = address_latitude;
    }

    public float getAddress_longitude() {
        return address_longitude;
    }

    public void setAddress_longitude(float address_longitude) {
        this.address_longitude = address_longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress_text() {
        return address_text;
    }

    public void setAddress_text(String address_text) {
        this.address_text = address_text;
    }

    public float getTotal_capacity() {
        return total_capacity;
    }

    public void setTotal_capacity(float total_capacity) {
        this.total_capacity = total_capacity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Details> getPayment_texts() {
        return payment_texts;
    }

    public void setPayment_texts(ArrayList<Details> payment_texts) {
        this.payment_texts = payment_texts;
    }

    public ArrayList<Details> getWorking_detail_texts() {
        return working_detail_texts;
    }

    public void setWorking_detail_texts(ArrayList<Details> working_detail_texts) {
        this.working_detail_texts = working_detail_texts;
    }
}
