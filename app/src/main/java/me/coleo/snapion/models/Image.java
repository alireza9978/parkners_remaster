package me.coleo.snapion.models;

public class Image {

    private int id;
    private int order;
    private InternalImage image;

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

    public InternalImage getImage() {
        return image;
    }

    public void setImage(InternalImage image) {
        this.image = image;
    }
}

class InternalImage {

    private int id;
    private String file;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}