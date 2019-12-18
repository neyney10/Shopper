package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.arielu.shopper.demo.models.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class UserShoppingListActivity extends AppCompatActivity {

    List<String> titles;
    TreeMap<String, ArrayList<Item>> list = new TreeMap<>();
    ExpandableListView ELV;
    BaseExpandableListAdapter ELA;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shopping_list);




        titles = new ArrayList<>(list.keySet());
       // titles.add("cat1");
        ELV = findViewById(R.id.user_list);
        ELA = new ListAdapter2(this,titles,list);
        ELV.setAdapter(ELA);
        //ELV.expandGroup(0);
    }
    public void myCart(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "worked", Toast.LENGTH_SHORT);
        toast.show();


    }

    public void btn_searchitemsClick(View view)
    {
        Intent intent = new Intent(this, SearchItemsActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Product result= (Product) data.getSerializableExtra("result");

                ArrayList<Item> temp;
                if(list.get(result.getItemType()) == null) {
                    temp = new ArrayList<>();

                } else {
                    temp = list.get(result.getItemType());
                }

                temp.add(new Item(result.getItemName(), 1337));
                list.put(result.getItemType(),temp);

                ELA.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
