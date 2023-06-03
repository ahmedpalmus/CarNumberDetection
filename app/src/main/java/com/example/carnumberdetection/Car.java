package com.example.carnumberdetection;

import java.io.Serializable;

public class Car implements Serializable {
    private String car_id, plate_number, color, brand, owner_name, owner_id, phone, details;

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
