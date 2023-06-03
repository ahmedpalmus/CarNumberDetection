package com.example.carnumberdetection;

import java.io.Serializable;

public class UserReport implements Serializable {
    private String problem_id, username,fullname, plate_number, scan_date, scan_image, details, lat, lon, state,response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getScan_date() {
        return scan_date;
    }

    public void setScan_date(String scan_date) {
        this.scan_date = scan_date;
    }

    public String getScan_image() {
        return scan_image;
    }

    public void setScan_image(String scan_image) {
        this.scan_image = scan_image;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
