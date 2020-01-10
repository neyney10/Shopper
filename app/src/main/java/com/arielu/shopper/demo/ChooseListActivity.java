package com.arielu.shopper.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.models.SessionProduct;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class ChooseListActivity extends AppCompatActivity implements DialogAddList.DialogListener
{
    ListView listview ;
    ArrayList<Shopping_list> shopping_Lists ;
    ArrayAdapter arrayAdapter;
    Button addList ;
    FirebaseAuth mAuth ;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        listview = (ListView) findViewById(R.id.lists) ;
        addList = (Button) findViewById((R.id.addListButton) );

        shopping_Lists = new ArrayList<Shopping_list>() ;
        mAuth = FirebaseAuth.getInstance();

        Firebase2.getUserLists(mAuth.getCurrentUser().getUid(),(lists) -> {
            addAllItemsToList((List<Shopping_list>)lists);
            arrayAdapter.notifyDataSetChanged();
        });

        Firebase2.getUserSharedLists(mAuth.getCurrentUser().getUid(),(lists) -> {
            addAllItemsToList(lists);
            arrayAdapter.notifyDataSetChanged();
        });


        this.arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1 ,shopping_Lists ) ;

        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ChooseListActivity.this, UserShoppingListActivity.class);

                Shopping_list listItem = shopping_Lists.get(i);
                intent.putExtra("list", listItem);
                //intent.putExtra("listID", listItem.getShopping_list_id());
                //intent.putExtra("listName", listItem.getShopping_list_title());
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode actionMode) {

                    }
                });
                return true;
            }
        });


        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = new DialogAddList();
                newFragment.show(getSupportFragmentManager(), "Add List");

            }

        });
    }


    private synchronized void addAllItemsToList(Collection<Shopping_list> list)
    {
        shopping_Lists.addAll(list);
    }


    @Override
    public void addList(String listName) {
            Shopping_list result = new Shopping_list("",mAuth.getCurrentUser().getUid(),listName);
            shopping_Lists.add(result);
            Firebase2.pushUserList(mAuth.getCurrentUser().getUid(),result);
            arrayAdapter.notifyDataSetChanged();
    }

}
