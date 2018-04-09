package com.example.capstone.furniturestore.Models;

import java.util.List;

/**
 * Created by amandeepsekhon on 2018-03-28.
 */

public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private List<Product> products ;

    public Request(String name, String phone, String address, String total, List<Product> products) {

        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.products = products;
    }

    public Request(String s, String string1, List<Product> cart) {

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
