package com.arielu.shopper.demo.database;


import com.arielu.shopper.demo.Item;
import com.arielu.shopper.demo.models.Product;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.utilities.DelegateonDataChangeFunction;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;

final public class Firebase {

    public static Observable<ArrayList<Product>> getProductList()
    {
         Observable o = firebaseDBGetRequest("test/root/Items/Item",(dataSnapshot) -> {
            ArrayList<HashMap<String,String>> items = (ArrayList<HashMap<String,String>>) dataSnapshot.getValue();
            ArrayList<Product> products = new ArrayList<>();

            for(int i =0;i<items.size();i++)
                products.add(Product.createFromMap(items.get(i)));

            return products;

        });

         return o;

    }

    public static Observable<ArrayList<Item>> getItemList()
    {
        Observable o = firebaseDBGetRequest("test/root/Items/Item",(dataSnapshot) -> {
            ArrayList<HashMap<String,String>> items_data = (ArrayList<HashMap<String,String>>) dataSnapshot.getValue();
            ArrayList<Item> items = new ArrayList<>();

            for(int i =0;i<items_data.size();i++)
                items.add(Item.createFromMap(items_data.get(i)));

            return items;

        });

        return o;

    }

    public static void setUserData(String uID, User user){
        firebaseDBSetRequest("users/"+uID, user);
    }


    public static Observable<User> getUserData(String uID)
    {
        Observable o = firebaseDBGetRequest("users/"+uID,(dataSnapshot) -> {
            HashMap<String,String> user_data = (HashMap<String,String>) dataSnapshot.getValue();
            User user = new User(user_data.get("username"),user_data.get("name"));

            return user;

        });

        return o;
    }


    public static void firebaseDBSetRequest(String refPath, Object value)
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference(refPath);

        myRef.setValue(value);
    }

    private static Observable firebaseDBGetRequest(String refPath, DelegateonDataChangeFunction func)
    {
        Observable o = Observable.create(emitter -> {
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // read from database
            DatabaseReference myRef = database.getReference(refPath);

            myRef.addListenerForSingleValueEvent(new ValueEventListenerTemplate(emitter,func));
        });

        return o;
    }

}
