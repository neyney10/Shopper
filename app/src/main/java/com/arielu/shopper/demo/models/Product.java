package com.arielu.shopper.demo.models;

import java.io.Serializable;
import java.util.Map;

import androidx.annotation.NonNull;

public class Product implements Serializable {
    private String itemCode;
    private String itemName;
    private String itemPrice;
    private String itemType;
    private boolean itemSelected;
    private String manufacturerName;
    private String qtyInPackage;

    public static Product createFromMap(Map<String,String> map)
    {
        Product prod  = new Product();

        prod.itemCode = map.get("ItemCode");
        prod.itemName = map.get("ItemName");
        prod.itemPrice = map.get("ItemPrice");
        prod.itemType = map.get("ItemType");
        prod.manufacturerName = map.get("ManufacturerName");
        prod.qtyInPackage = map.get("QtyInPackage");

        return prod;
    }
    //Getter
    public String getItemName()
    {
        return itemName;
    }
    public String getItemType()
    {
        return itemType;
    }
    public boolean isItemSelected() {
        return itemSelected;
    }
    //Setter
    public void setItemSelected(boolean itemSelected) {
        this.itemSelected = itemSelected;
    }

    @NonNull
    @Override
    public String toString() {
       /* return "(Item code: "   + itemCode  +
                ", name: "      + itemName  +
                ", price: "     + itemPrice +
                ", type: "      + itemType  +
                ", manu_name: " + manufacturerName +
                ", qty: "       + qtyInPackage;

        */

       return "("+itemName+")";
    }
}
