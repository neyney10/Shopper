package com.arielu.shopper.demo.classes;

public class Shopping_list
{
    int shopping_list_id ;
    int user_id ;
    String shopping_list_title ;

    public Shopping_list(int shopping_list_id, int user_id, String shopping_list_title) {
        this.shopping_list_id = shopping_list_id;
        this.user_id = user_id;
        this.shopping_list_title = shopping_list_title;
    }

    public int getShopping_list_id() {
        return shopping_list_id;
    }

    public void setShopping_list_id(int shopping_list_id) {
        this.shopping_list_id = shopping_list_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getShopping_list_title() {
        return shopping_list_title;
    }

    public void setShopping_list_title(String shopping_list_title) {
        this.shopping_list_title = shopping_list_title;
    }
}
