package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.models.Product;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Override
    protected void onStart() {
        super.onStart();

        TextView txt_name = findViewById(R.id.txt_name);
        txt_name.setTextColor(Color.rgb(211,211,211));
        Observable<User> o = Firebase.getUserData(mAuth.getCurrentUser().getUid());
        o.subscribe(new ObserverFirebaseTemplate<User>() {
                        @Override
                        public void onNext(User user) {
                            txt_name.setText(user.getName() + " (" + user.getUsername() + ")");
                            txt_name.setTextColor(Color.BLACK);
                        }
                    });
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

    public void onListBtnClick(View view)
    {
        Intent intent = new Intent(this, UserShoppingListActivity.class);
        startActivity(intent);
    }
}