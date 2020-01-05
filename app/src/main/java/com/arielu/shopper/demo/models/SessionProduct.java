package com.arielu.shopper.demo.models;

import com.arielu.shopper.demo.classes.Product;

public class SessionProduct extends Product {

    //////////////////////////////
    ///////// Properties /////////
    //////////////////////////////

    protected Boolean isBought = false;

    //////////////////////////////
    //////// Constructors ////////
    //////////////////////////////

    // Empty Constructor for firebase
    private SessionProduct() {}

    public SessionProduct(String productCode , String categoryName , String productName, Double productPrice, String productImageUrl, String productManufacturer)
    {
        super(productCode,categoryName,productName,productPrice,productImageUrl,productManufacturer);
    }

    // Copy Constructor
    public SessionProduct(Product otherProd)
    {
        super(otherProd);
    }

    public SessionProduct(SessionProduct other)
    {
        super(other);
        setIsBought(other.getIsBought());
    }

    /////////////////////////////////////
    ///////// Getters & Setters /////////
    /////////////////////////////////////

    public Boolean getIsBought() { return isBought; }
    public void setIsBought(Boolean bought) { isBought = bought; }
}
