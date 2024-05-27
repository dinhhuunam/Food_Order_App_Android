package com.project.foodApp.Model;

import java.util.List;

public class Order {
    private String phone; //số điện thoại người mua
    private String name; //tên người mua
    private String address; //địa chỉ người mua
    private String total;


    private String date;
    public Order() {
    }

    public Order(String phone, String name, String address, String total, String date) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}