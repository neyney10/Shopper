package com.arielu.shopper.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChooseListActivity extends AppCompatActivity implements DialogAddList.DialogListener
{
    ListView listview ;
    ArrayList<Shopping_list> shopping_Lists ;
    ListsAdapter arrayAdapter;
    Toolbar toolbar;
    LinearLayout addListFAB;
    FirebaseAuth mAuth ;
    private SearchView searchView;
    private boolean isSelectOn;
    private int blue,white;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        listview = (ListView) findViewById(R.id.lists) ;

        shopping_Lists = new ArrayList<Shopping_list>() ;
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);

        addListFAB = findViewById(R.id.add_list_button);

        searchView = findViewById(R.id.lists_filter);
        blue = ContextCompat.getColor(getApplicationContext(),R.color.blue);
        white = ContextCompat.getColor(getApplicationContext(), R.color.white);
        /* deprecated
        Observable<List<Shopping_list>> o =  Firebase.getUserLists(mAuth.getCurrentUser().getUid());
        o.subscribe(new ObserverFirebaseTemplate<List<Shopping_list>>() {
            @Override
            public void onNext(List<Shopping_list> lists) {
                shopping_Lists.clear();
                shopping_Lists.addAll(lists);
                arrayAdapter.notifyDataSetChanged();
            }
        });
         */

        Firebase2.getUserLists(mAuth.getCurrentUser().getUid(),(lists) -> {
            addAllItemsToList((List<Shopping_list>)lists);
            arrayAdapter.notifyDataSetChanged();
        });

        Firebase2.getUserSharedLists(mAuth.getCurrentUser().getUid(),(lists) -> {
            addAllItemsToList(lists);
            arrayAdapter.notifyDataSetChanged();
        });


        this.arrayAdapter = new ListsAdapter(this,shopping_Lists);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isSelectOn){
                    if (arrayAdapter.select(i))
                        view.setBackgroundColor(blue);
                    else view.setBackgroundColor(white);
                    if (arrayAdapter.allSelectedItems()==0)
                        selectMode(false);
                    toolbar.setTitle(arrayAdapter.allSelectedItems()+" items Selected");
                }else {
                    Intent intent = new Intent(ChooseListActivity.this, UserShoppingListActivity.class);

                    Shopping_list listItem = shopping_Lists.get(i);
                    intent.putExtra("list", listItem);
                    startActivity(intent);
                }
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectMode(true);
                arrayAdapter.select(i);
                toolbar.setTitle(arrayAdapter.allSelectedItems()+" items Selected");
                view.setBackgroundColor(blue);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ChooseListActivity.this.arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
    public void createNewList(View v){
        DialogFragment newFragment = new DialogAddList();
        newFragment.show(getSupportFragmentManager(), "Add List");

    }

    private synchronized void addAllItemsToList(Collection<Shopping_list> list)
    {
        shopping_Lists.addAll(list);
    }
    //delegate between dialog and activity
    @Override
    public void addList(String listName) {
            Shopping_list result = new Shopping_list("",mAuth.getCurrentUser().getUid(),listName);
            shopping_Lists.add(result);
            Firebase2.pushUserList(mAuth.getCurrentUser().getUid(),result);
            arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_mode_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                remove();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSelectOn) {
            selectMode(false);
            cancel();
        }
        else
            super.onBackPressed();
    }
    private void cancel(){
        arrayAdapter.cancel();
    }
    private void remove(){
        Toast.makeText(getApplicationContext(),arrayAdapter.allSelectedItems()+" items were deleted.",Toast.LENGTH_SHORT);
        ArrayList<String> listIds = (ArrayList<String>) arrayAdapter.remove();
        selectMode(false);
        Firebase2.removeListItems(listIds);
        Firebase2.setUserLists(mAuth.getCurrentUser().getUid(),shopping_Lists);

    }

    private void selectMode(boolean state){
        isSelectOn=state;
        if (state) {
            toolbar.setVisibility(View.VISIBLE);
            addListFAB.setVisibility(View.GONE);
        }
        else {
            toolbar.setVisibility(View.GONE);
            addListFAB.setVisibility(View.VISIBLE);
        }
    }
}
