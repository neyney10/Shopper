package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

    }

    private void expandAll()
    {
        for(int i = 0; i < list.size();i++)
        {
            ELV.expandGroup(i);
        }
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


    public void btn_saveClick(View view)
    {
        Collection<ArrayList<Product>> c = list.values();
        ArrayList<Product> lst = new ArrayList<>();
        for(ArrayList<Product> l : c ) { lst.addAll(l); }
        Firebase.setListProducts(listID,lst);

        Toast.makeText(UserShoppingListActivity.this,"Saving your list...",Toast.LENGTH_SHORT);
    }


    public void btn_permissionsClick(View view)
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

                ELA.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}



   