package com.arielu.shopper.demo.database;


import com.arielu.shopper.demo.Item;
import com.arielu.shopper.demo.models.Permission;
import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.utilities.DelegateonDataChangeFunction;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

final public class Firebase {

    public static Observable<List<Product>> getProductList()
    {
         Observable o = firebaseDBGetRequest("products",(dataSnapshot) -> {
            GenericTypeIndicator<List<Product>> genericTypeIndicator = new GenericTypeIndicator<List<Product>>() {};
            List<Product> products = dataSnapshot.getValue(genericTypeIndicator);

            return products;

        });

         return o;

    }

    public static Observable<List<Product>> getListItems(String listID)
    {
        Observable o = firebaseDBGetRequest("shopping_list_items/"+listID,(dataSnapshot) -> {
            GenericTypeIndicator<List<Product>> genericTypeIndicator = new GenericTypeIndicator<List<Product>>() {};
            List<Product> items_data = dataSnapshot.getValue(genericTypeIndicator);

            return items_data;

        });

        return o;

    }

    public static void setUserData(String uID, User user){
        firebaseDBSetRequest("users/"+uID, user);
    }

    public static Observable<List<Permission>> getListPermissions(String listID)
    {
        Observable o = firebaseDBGetRequest("shopping_list_permissions/"+listID,(dataSnapshot) -> {
            GenericTypeIndicator<List<Permission>> genericTypeIndicator = new GenericTypeIndicator<List<Permission>>() {};
            List<Permission> permissions_data = dataSnapshot.getValue(genericTypeIndicator);

            return permissions_data;
        });

        return o;
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

    // Save list
    public static void setListProducts(String listID, List<Product> listItems)
    {
        firebaseDBSetRequest("shopping_list_items/"+listID, listItems);
    }


    private static void firebaseDBSetRequest(String refPath, Object value)
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference(refPath);

        myRef.setValue(value);
    }

    private static Observable firebaseDBGetRequest(String refPath, DelegateonDataChangeFunction func)
    {
        Observable o = Observable.create(emitter -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // read from database
            DatabaseReference myRef = database.getReference(refPath);

            myRef.addListenerForSingleValueEvent(new ValueEventListenerTemplate(emitter,func));
        });

        return o;
    }

}
