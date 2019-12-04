package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
            // sign out
            mAuth.signOut();

            // switch activity to login form.
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void databaseTestClick(View view)
    {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");


        myRef.child(mAuth.getCurrentUser().getUid()).child("name").setValue("Nikolai");


        // read from database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Firebase", "data retrieved: "+dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }
}
