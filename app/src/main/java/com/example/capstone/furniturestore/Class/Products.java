package com.example.capstone.furniturestore.Class;

import java.util.PriorityQueue;

/**
 * Created by tejalpatel on 2018-03-11.
 */

public class Products {
    private String ProductBrand;
    private String ProductColour;
    private String ProductDepartment;
    private Integer ProductId;
    private String ProductMenufacturer;
    private String ProductName;
    private Float ProductPrice;
    private Integer ProductQuntity;
    private String ProductStyle;
    private String ProductOffer;


    public Products(){

    }

    public Products(Integer productId, String productName, Integer productQunt,String productBrand, String productColour, String productMenufacturer,Float productPrice, String productStyle, String productOffer,String productDepartment) {
        this.ProductId = productId;
        this.ProductName = productName;
        this.ProductQuntity = productQunt;
        this.ProductBrand = productBrand;
        this.ProductColour = productColour;
        this.ProductMenufacturer = productMenufacturer;
        this.ProductPrice = productPrice;
        this.ProductStyle = productStyle;
        this.ProductOffer = productOffer;
        this.ProductDepartment = productDepartment;
    }

    public Integer getProductId() {
        return ProductId;
    }
    public String getProductName() {
        return ProductName;
    }
    public Integer getProductQunt() {
        return ProductQuntity;
    }
    public String getProductColour() {
        return ProductColour;
    }
    public String getProductBrand() {
        return ProductBrand;
    }

    public String getProductManufacturer() {
        return ProductMenufacturer;
    }
    public Float getProductPrice() {
        return ProductPrice;
    }
    public String getProductStyle() {
        return ProductStyle;
    }
    public String getProductOffer() {
        return ProductOffer;
    }
    public String getProductDepartment() {
        return ProductDepartment;
    }
}
