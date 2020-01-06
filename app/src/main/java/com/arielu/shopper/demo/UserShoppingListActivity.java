package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arielu.shopper.demo.classes.ImageDownloader;
import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.pinnedsectionlistview.PinnedSectionAdapter;
import com.arielu.shopper.demo.pinnedsectionlistview.PinnedSectionListView;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class UserShoppingListActivity extends AppCompatActivity {

    private TreeMap<String, ArrayList<Product>> list = new TreeMap<>();
    private ExpandableListView ELV;
    private BaseExpandableListAdapter ELA;

    private Toolbar toolbar;
    private SearchView searchView;
    private PinnedSectionAdapter pinnedSectionAdapter;
    private PinnedSectionListView pinnedSectionListView;
    private boolean isSelectOn;
    private int blue,white;

    private String listID;
    private String listName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shopping_list);
        // need a policy to allow downloading / accessing images from the network
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.listID = getIntent().getStringExtra("listID");
        this.listName = getIntent().getStringExtra("listName");
        //my edit
        blue = ContextCompat.getColor(getApplicationContext(),R.color.blue);
        white = ContextCompat.getColor(getApplicationContext(), R.color.white);
        //toolbar
        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(listName);
        //Filter
        searchView = findViewById(R.id.list_filter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                UserShoppingListActivity.this.pinnedSectionAdapter.getFilter().filter(newText);
                expandAll();
                return false;
            }
        });
        //PinnedSectionListView
        pinnedSectionAdapter = new PinnedSectionAdapter(this,list);
        pinnedSectionListView = findViewById(R.id.user_list);
        pinnedSectionListView.setAdapter(pinnedSectionAdapter);
        pinnedSectionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(pinnedSectionListView.getPackedPositionType(l)== PinnedSectionListView.PACKED_POSITION_TYPE_CHILD){
                    int group = pinnedSectionListView.getPackedPositionGroup(l);
                    int child = pinnedSectionListView.getPackedPositionChild(l);
                    //TODO function set select mode for delete,new list,sum of selected items,etc...
                    selectMode(true);
                    view.setBackgroundColor(blue);
                    ((PinnedSectionAdapter)((PinnedSectionListView)adapterView).getExpandableListAdapter()).select(group,child);
                }
                return true;
            }
        });
        pinnedSectionListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (isSelectOn){
                    if (((PinnedSectionAdapter)expandableListView.getExpandableListAdapter()).select(i,i1))
                        view.setBackgroundColor(blue);
                    else
                        view.setBackgroundColor(white);
                    if (pinnedSectionAdapter.selectedItems.isEmpty())
                        selectMode(false);
                }else {
                    Product product = (Product) expandableListView.getExpandableListAdapter().getChild(i, i1);
                }
                return true;
            }
        });
        pinnedSectionListView.setPinnedSections(R.layout.list_group);
        //get data
        Observable o = Firebase.getListItems(this.listID);
        o.subscribe(new ObserverFirebaseTemplate<List<Product>>() {
            @Override
            public void onNext(List<Product> products) {
                ArrayList<Product> temp;
                for(Product p : products)
                {
                    // get image
                    p.setProductImage(ImageDownloader.getBitmapFromURL(p.getProductImageUrl()));

                    if(list.get(p.getCategoryName()) == null) {
                        temp = new ArrayList<>();

                    } else {
                        temp = list.get(p.getCategoryName());
                    }

                    temp.add(p);
                    list.put(p.getCategoryName(),temp);
                }
                pinnedSectionAdapter.updateList();
                expandAll();
            }
        });
/*
        TextView tv_listname = findViewById(R.id.tv_listname);
        tv_listname.setText(this.listName);


        ELV = findViewById(R.id.user_list);
        ELA = new ListAdapter2(this,list);
        ELV.setAdapter(ELA);


        Observable o = Firebase.getListItems(this.listID);
        o.subscribe(new ObserverFirebaseTemplate<List<Product>>() {
            @Override
            public void onNext(List<Product> products) {
                ArrayList<Product> temp;
                for(Product p : products)
                {
                    // get image
                    p.setProductImage(ImageDownloader.getBitmapFromURL(p.getProductImageUrl()));

                    if(list.get(p.getCategoryName()) == null) {
                        temp = new ArrayList<>();

                    } else {
                        temp = list.get(p.getCategoryName());
                    }

                    temp.add(p);
                    list.put(p.getCategoryName(),temp);
                }

                ELA.notifyDataSetChanged();
                expandAll();
            }
        });
*/
    }
//new

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isSelectOn)
            getMenuInflater().inflate(R.menu.select_mode_menu,menu);
        else
            getMenuInflater().inflate(R.menu.user_list_menu,menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                btn_searchitemsClick();
                return true;
            case R.id.save_list:
                btn_saveClick();
                return true;
            case R.id.user_permission:
                btn_permissionsClick();
                return true;
            case R.id.delete:
                pinnedSectionAdapter.remove();
                selectMode(false);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
    private void selectMode(boolean state){
        isSelectOn=state;
        invalidateOptionsMenu();
        pinnedSectionAdapter.notifyDataSetChanged();
    }
    private void expandAll()
    {
        for(int i = 0; i < list.size();i++)
        {
            pinnedSectionListView.expandGroup(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSelectOn) {
            pinnedSectionAdapter.selectedItems.clear();
            selectMode(false);
        }else
        super.onBackPressed();
    }

    public void myCart(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "worked", Toast.LENGTH_SHORT);
        toast.show();


    }


    public void btn_searchitemsClick()
    {
        Intent intent = new Intent(this, SearchItemsActivity.class);
        startActivityForResult(intent,1);
    }


    public void btn_saveClick()
    {
        Collection<ArrayList<Product>> c = list.values();
        ArrayList<Product> lst = new ArrayList<>();
        for(ArrayList<Product> l : c ) { lst.addAll(l); }
        Firebase.setListProducts(listID,lst);

        Toast.makeText(UserShoppingListActivity.this,"Saving your list...",Toast.LENGTH_SHORT);
    }


    public void btn_permissionsClick()
    {
        // TODO
        // intent -> change activity to ChangePermissionListActivity
        // putting the list id in putextra.
        // inside that activity, load the current permitted user from the DB
        // and make an option to change the list -> add new users / remove users.
        // adding new user by their (?)
        Intent intent = new Intent(this, ShoppingListPermissionsActivity.class);
        intent.putExtra("listID", this.listID);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Product result= Product.fromBundle(data.getParcelableExtra("result"));
                result.setProductImage(ImageDownloader.getBitmapFromURL(result.getProductImageUrl()));


                ArrayList<Product> temp;
                Boolean isCategoryExistInView = (list.get(result.getCategoryName()) == null);
                if(isCategoryExistInView) {
                    temp = new ArrayList<>();
                    list.put(result.getCategoryName(), temp);

                } else {
                    temp = list.get(result.getCategoryName());
                }

                if(!temp.contains(result))
                    temp.add(result);

                pinnedSectionAdapter.updateList();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}



   