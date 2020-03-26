package me.coleo.snapion.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * کلاس پارکینگ
 */
public class Parking implements Serializable {

    private int id;
    private String title;
    private String address_text;
    private float total_capacity;
    private float address_latitude;
    private float address_longitude;
    private String capacity;
    private int capacity_bar;
    private String distance;
    private ArrayList<Details> payment_texts;
    private ArrayList<Details> working_detail_texts;
    private ArrayList<Image> images;
    private String share_text;

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", address_text='" + address_text + '\'' +
                ", total_capacity=" + total_capacity +
                ", address_latitude=" + address_latitude +
                ", address_longitude=" + address_longitude +
                ", distance='" + distance + '\'' +
                '}';
    }

    public float getProgress() {
        if (capacity_bar == 0) {
            return 0;
        }
        return (capacity_bar * 20f) - 1;
    }

    public String getShare_text() {
        return share_text;
    }

    public void setShare_text(String share_text) {
        this.share_text = share_text;
    }

    public int getCapacity_bar() {
        return capacity_bar;
    }

    public void setCapacity_bar(int capacity_bar) {
        this.capacity_bar = capacity_bar;
    }

    public String getDistance() {
        if (distance == null) {
            return "ناموجود";
        }
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getCapacityText() {
        return capacity;
    }

    public String getPricesString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < payment_texts.size(); i++) {
            result.append(payment_texts.get(i).getText());
            result.append("\n");
        }
        return result.toString();
    }


    public String getWorkHoursString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < working_detail_texts.size(); i++) {
            result.append(working_detail_texts.get(i).getText());
            result.append("\n");
        }
        return result.toString();
    }


    public ArrayList<String> getImageURLs() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            result.add(images.get(i).getImage().getFile());
        }
        return result;
    }

}
