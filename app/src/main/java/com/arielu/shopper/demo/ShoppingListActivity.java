package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ShoppingListActivity extends AppCompatActivity {
   Item[] items;
   List<String> titles;
   TreeMap<String,Item[]> list = new TreeMap<>();
    ItemAdapter itemAdapter;
    ListView itemList;
    ExpandableListView ELV;
    ExpandableListAdapter ELA;


    public void generateItems() {
        items = new Item[20];
        for (int i = 0; i < 20; i++) {
            items[i] = new Item("Item"+i,Math.round((Math.random()*10)*100)/100.0);
        }
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_layout);
        generateItems();
        /*
        itemAdapter  = new ItemAdapter(items,ShoppingListActivity.this);
        itemList = (ListView) findViewById(R.id.user_list);
        itemList.setAdapter(itemAdapter);
         */
        for (int i = 0; i <4; i++) {
            list.put("category"+i,items);
        }
        titles = new ArrayList<>(list.keySet());
        ELV = findViewById(R.id.user_list);
        ELA = new ListAdapter(this,titles,list);
        ELV.setAdapter(ELA);
        ELV.expandGroup(0);
    }
    public void myCart(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "worked", Toast.LENGTH_SHORT);
        toast.show();


    }

}
