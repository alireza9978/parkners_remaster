package me.coleo.snapion.models;

/**
 * کلاس کاربر
 */
public class User {

    private int id;
    private String key;

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", key='" + key + '\'' +
                '}';
    }
}
