package me.coleo.snapion.models;

public class Details {

    private int id;
    private int order;
    private String text;

    public Details(int id, int order, String text) {
        this.id = id;
        this.order = order;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
