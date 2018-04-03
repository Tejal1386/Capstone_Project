package com.example.capstone.furniturestore.Models;

/**
 * Created by tejalpatel on 2018-03-19.
 */

public class Product {

    private String ProductID;
    private String ProductName;
    private String ProductImage;
    private double ProductPrice;
    private String ProductPricenew;
    private double ProductSalePrice;
    private String ProductQunt;
    private String ProductBrand;
    private String ProductColor;
    private String ProductDept;
    private String ProductStyle;
    private String ProductManufacturer;
    private String ProductOffer;
    private String ProductCatID;
    private String number;

    public Product(String productId, String productName,  String productPrice,String productQuantity, String productOffer,String productImage) {
        ProductID = productId;
        ProductName = productName;
        ProductPricenew = productPrice;
        ProductQunt = productQuantity;
        ProductOffer = productOffer;
        ProductImage =productImage;

    }

 /*   public Product(String productID, String productName, String productImage, double productPrice, double productSalePrice, String productQunt, String productBrand, String productColor, String productDept, String productStyle, String productManufacturer, String productOffer, String productCatID) {
        ProductID = productID;
        ProductName = productName;
        ProductImage = productImage;
        ProductPrice = productPrice;
        ProductSalePrice = productSalePrice;
        ProductQunt = productQunt;
        ProductBrand = productBrand;
        ProductColor = productColor;
        ProductDept = productDept;
        ProductStyle = productStyle;

        ProductManufacturer = productManufacturer;
        ProductOffer = productOffer;
        ProductCatID = productCatID;
    }
*/


    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {

    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public String getProductPricenew() {
        return ProductPricenew;
    }

    public void setProductPricenew(String productPricenew) {
        ProductPricenew = productPricenew;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public double getProductSalePrice() {
        return ProductSalePrice;
    }

    public void setProductSale_Price(double productSalePrice) {
        ProductSalePrice = productSalePrice;

    }

    public String getProductQunt() {
        return ProductQunt;
    }

    public void setProductQunt(String productQunt) {
        ProductQunt = productQunt;
    }

    public String getProductBrand() {
        return ProductBrand;
    }

    public void setProductBrand(String productBrand) {
        ProductBrand = productBrand;
    }

    public String getProductColor() {
        return ProductColor;
    }

    public void setProductColor(String productColor) {
        ProductColor = productColor;
    }

    public String getProductDept() {
        return ProductDept;
    }

    public void setProductDept(String productDept) {
        ProductDept = productDept;
    }

    public String getProductStyle() {
        return ProductStyle;
    }

    public void setProductStyle(String productStyle) {
        ProductStyle = productStyle;
    }


    public String getProductManufacturer() {
        return ProductManufacturer;
    }

    public void setProductManufacturer(String productManufacturer) {
        ProductManufacturer = productManufacturer;
    }

    public String getProductOffer() {
        return ProductOffer;
    }

    public void setProductOffer(String productOffer) {
        ProductOffer = productOffer;
    }

    public String getProductCatID() {
        return ProductCatID;
    }

    public void setProductCatID(String productCatID) {
        ProductCatID = productCatID;
    }

    public String getnumber() {
        return number;
    }

    public Product() {
    }
}
