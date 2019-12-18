package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.models.Permission;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListPermissionsActivity extends AppCompatActivity {

    private String listID;
    private List<Permission> permissions;

    // UI
    private ListView lv_permissions;
    private ArrayAdapter<Permission>  lv_permissions_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_permissions);

        this.listID = getIntent().getStringExtra("listID");

        this.permissions = new ArrayList<>();

        // arrange listview and its adapter.
        lv_permissions = findViewById(R.id.lv_permissions);
        lv_permissions_adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                permissions );
        lv_permissions.setAdapter(lv_permissions_adapter);

        // Firebase DB get permission list for list id
        Observable o = Firebase.getListPermissions(this.listID);
        o.subscribe(new ObserverFirebaseTemplate<List<Permission>>() {
            @Override
            public void onNext(List<Permission> permList) {
                permissions.clear();
                permissions.addAll(permList);
                lv_permissions_adapter.notifyDataSetChanged();
                Log.d("Permissions_List", permissions.toString());
            }
        });




    }


}
