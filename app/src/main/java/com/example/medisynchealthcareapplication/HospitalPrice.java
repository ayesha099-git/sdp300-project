package com.example.medisynchealthcareapplication;
public class HospitalPrice {
    private String hospitalName;
    private double price;

    public HospitalPrice(String hospitalName, double price) {
        this.hospitalName = hospitalName;
        this.price = price;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public double getPrice() {
        return price;
    }
}
