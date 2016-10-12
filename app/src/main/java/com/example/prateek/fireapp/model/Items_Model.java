package com.example.prateek.fireapp.model;

public class Items_Model {

    String name, value, time, type;

    public Items_Model() {

    }

    public Items_Model(String name, String value, String time, String type) {
        this.name = name;
        this.value = value;
        this.time = time;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
