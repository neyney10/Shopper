package com.arielu.shopper.demo.database;


import android.util.Log;

import com.arielu.shopper.demo.classes.Branch;
import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.models.Message;
import com.arielu.shopper.demo.models.Permission;
import com.arielu.shopper.demo.models.SessionProduct;
import com.arielu.shopper.demo.models.StoreProductRef;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.utilities.DelegateonDataChangeFunction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.QueryParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

final public class Firebase2 {

    public static void getListItems(String listID, Task task)
    {
        firebaseDBGetRequest("shopping_list_items/"+listID, task, (dataSnapshot) -> {
            GenericTypeIndicator<List<SessionProduct>> genericTypeIndicator = new GenericTypeIndicator<List<SessionProduct>>() {};
            try{
                List<SessionProduct> items_data = dataSnapshot.getValue(genericTypeIndicator);
                return items_data;
            }
            catch (Exception e)
            {
                return new ArrayList<SessionProduct>();
            }
        });
    }

    public static void getUserListinSession(String uID, Task task)
    {
        firebaseDBGetRequest("user_session_lists/"+uID,task,(dataSnapshot) -> {
            List<String> lists = new ArrayList<>();
            for(DataSnapshot dss : dataSnapshot.getChildren())
                lists.add(dss.getValue(String.class));

            return lists;
        });
    }

    public static void getProductList(Task task)
    {
        firebaseDBGetRequest("products",task,(dataSnapshot) -> {
            GenericTypeIndicator<List<Product>> genericTypeIndicator = new GenericTypeIndicator<List<Product>>() {};
            List<Product> products = dataSnapshot.getValue(genericTypeIndicator);

            return products;

        });
    }

    public static void getStoreMessages(String companybranchID, Task task)
    {
        firebaseDBGetRequest("store_message/"+companybranchID, task, (dataSnapshot) -> {
            List<Message> messages = new ArrayList<>();
            for(DataSnapshot dss : dataSnapshot.getChildren())
                messages.add(dss.getValue(Message.class));

            return messages;
        });
    }

    public static void getStoresList(Task task)
    {
        firebaseDBGetRequest("stores", task, (dataSnapshot) -> {
            GenericTypeIndicator<List<Branch>> genericTypeIndicator = new GenericTypeIndicator<List<Branch>>() {};
            List<Branch> lists = dataSnapshot.getValue(genericTypeIndicator);

            return lists;
        });

    }

    public static void getUserLists(String uID, Task task)
    {
        firebaseDBGetRequest("user_shopping_lists/"+uID, task, (dataSnapshot) -> {
            GenericTypeIndicator<List<Shopping_list>> genericTypeIndicator = new GenericTypeIndicator<List<Shopping_list>>() {};
            List<Shopping_list> lists = dataSnapshot.getValue(genericTypeIndicator);

            return lists;
        });
    }

    public static void getUserData(String uID, Task task)
    {
        firebaseDBGetRequest("users/"+uID, task, (dataSnapshot) -> {
            User user = dataSnapshot.getValue(User.class);
            return user;
        });
    }


    // Save list
    public static void setListProducts(String listID, List<SessionProduct> listItems)
    {
        firebaseDBSetRequest("shopping_list_items/"+listID, listItems);
    }

    // Save/post message
    public static void pushNewStoreMessage(String companybranchID, Message message)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("store_message/"+companybranchID);

        String pushedKey = myRef.push().getKey();

        Map<String, Object> map = new HashMap<>();
        map.put(pushedKey, message);
        myRef.updateChildren(map);
    }


    ////////////// Templates //////////////////

    private static void firebaseDBSetRequest(String refPath, Object value)
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference(refPath);

        myRef.setValue(value);
    }

    private static void firebaseDBGetRequestWithQuery(Query query, Task task, DelegateonDataChangeFunction func)
    {
        query.addListenerForSingleValueEvent(new ValueEventListenerTemplate2(func, task));
    }

    private static void firebaseDBGetRequest(String refPath, Task task, DelegateonDataChangeFunction func)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // read from database
        DatabaseReference myRef = database.getReference(refPath);

        firebaseDBGetRequestWithQuery(myRef, task, func);
    }


    // for lambda exp
    public interface Task<T>
    {
        public void execute(T data);
    }

}
