package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.models.Product;
import com.arielu.shopper.demo.models.User;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserPanelActivity extends AppCompatActivity {

    //// Firebase authentication ////
    private FirebaseAuth mAuth;
    EditText editTextName ;
    Spinner list ;
    Button addList ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();



        //
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

    @Override
    protected void onStart() {
        super.onStart();

        test();

    }

    public void test() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("test/root/Items/Item");

        Query query = reference.orderByChild("ItemPrice").equalTo("6.40");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        //Intent intent = new Intent(this, UserShoppingListActivity.class);
        Intent intent = new Intent(this, ChooseListActivity.class);

//        intent.putExtra("listID","0");
//        intent.putExtra("listName","My LiSt!");
        startActivity(intent);
    }
}
