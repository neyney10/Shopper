package com.arielu.shopper.demo.classes;

public class Shopping_list_products_branches
{
    int shopping_list_id ;
    int product_id ;
    int branch_id ;

    public Shopping_list_products_branches(int shopping_list_id, int product_id, int branch_id) {
        this.shopping_list_id = shopping_list_id;
        this.product_id = product_id;
        this.branch_id = branch_id;
    }

    public int getShopping_list_id() {
        return shopping_list_id;
    }

    public void setShopping_list_id(int shopping_list_id) {
        this.shopping_list_id = shopping_list_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }
}
