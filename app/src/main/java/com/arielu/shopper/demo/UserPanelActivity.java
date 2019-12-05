package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.models.Product;
import com.arielu.shopper.demo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class UserPanelActivity extends AppCompatActivity {


    //// Firebase authentication ////
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }


    public void signOutClick(View view)
    {
        // Check the user is indeed logged in
        if(mAuth.getCurrentUser() != null)
        {
            // sign out.
            mAuth.signOut();

            // switch activity to login form.
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void databaseTest2Click(View view)
    {
        Observable o = Firebase.getProductList();
        o.subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("observer","1");
            }

            @Override
            public void onNext(Object o) {
                Log.d("observer","2");
                Log.d("observer",o.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("observer","3");

            }

            @Override
            public void onComplete() {
                Log.d("observer","4");
            }
        });
    }

    public void databaseTestClick(View view)
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.child(mAuth.getCurrentUser().getUid()).setValue(new User("customer1@shopper.co.il","Nikolai"));

        // read from database
        myRef = database.getReference("user_shopping_lists"+"/"+mAuth.getCurrentUser().getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Firebase", "data retrieved: "+dataSnapshot.getValue());
                ArrayList<String> user_lists = (ArrayList<String>) dataSnapshot.getValue();
                Log.d("TestDebug", "user_lists:" +user_lists.toString());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("shopping_list"+"/"+String.valueOf(user_lists.get(1)));

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Firebase", "data retrieved: "+dataSnapshot.getValue());
                        HashMap<String,String> products = (HashMap<String,String>) dataSnapshot.getValue();
                        Log.d("TestDebug", "products:" +products.toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


    }
}
