package com.arielu.shopper.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class ChooseListActivity extends AppCompatActivity
{
    ListView listview ;
    DatabaseReference dataBaseLists ;
    DataSnapshot dataSnapshot ;
    ArrayList<Shopping_list> shopping_Lists ;
    ArrayAdapter arrayAdapter;
    Button addList ;
    FirebaseAuth mAuth ;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        listview = (ListView) findViewById(R.id.lists) ;
        addList = (Button) findViewById((R.id.addList) );

        shopping_Lists = new ArrayList<Shopping_list>() ;
        mAuth = FirebaseAuth.getInstance();

        Observable<List<Shopping_list>> o =  Firebase.getUserLists(mAuth.getCurrentUser().getUid());
        o.subscribe(new ObserverFirebaseTemplate<List<Shopping_list>>() {
            @Override
            public void onNext(List<Shopping_list> lists) {
                shopping_Lists.clear();
                shopping_Lists.addAll(lists);
                arrayAdapter.notifyDataSetChanged();
            }
        });


        this.arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 ,shopping_Lists ) ;

        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ChooseListActivity.this, UserShoppingListActivity.class);

                Shopping_list listItem = shopping_Lists.get(i);
                intent.putExtra("listID", listItem.getShopping_list_id());
                intent.putExtra("listName", listItem.getShopping_list_title());
                startActivity(intent);
            }
        });


//        //NOT READY!!!!! - problem: how to retrieve user id ??
//        //add new list to database
//        addList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String id = dataBaseLists.push().getKey() ;
//                Shopping_list list = new Shopping_list( id , id , " s") ;
//                dataBaseLists.child(String.valueOf(id)).setValue(list);
//                Toast.makeText(ChooseListActivity.this , "clicked" , Toast.LENGTH_LONG).show();
//            }
//
//
//        });
    }

}
