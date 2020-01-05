package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;

import java.util.ArrayList;
import java.util.List;

public class SearchItemsActivity extends AppCompatActivity {


    private List<Product> products;
    private List<Product> products_filtered;
    private ArrayAdapter<Product> adapter;
    private Product selected_item;

    // UI elements //
    private ListView lv_products_filtered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        products = new ArrayList<>(2);
        products_filtered = new ArrayList<>(30);

        LinkUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /* DEPRECATED
        Observable<List<Product>> o = Firebase.getProductList();
        o.subscribe(new ObserverFirebaseTemplate<List<Product>>() {
            @Override
            public void onNext(List<Product> prods) {
                products = prods;
            }
        });
        */

        Firebase2.getProductList((prods)->{
            products = (List<Product>) prods;
        });

    }

    protected void LinkUI()
    {
        // listview products filtered
        lv_products_filtered = findViewById(R.id.lv_products_filtered);
        lv_products_filtered.setOnItemClickListener(this::onItemClick);

        adapter = new ArrayAdapter<Product>(SearchItemsActivity.this,R.layout.item_test_sample,R.id.sample_text_view,products_filtered);
        lv_products_filtered.setAdapter(adapter);

        // searchbox
        SearchView sv_search = findViewById(R.id.sv_search);
        

        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                products_filtered.clear();
                for(Product p : products)
                {
                    if(p.getProductName().contains(s))
                        products_filtered.add(p);
                }
                adapter.notifyDataSetChanged();

                return false;
            }
        });



    }

    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
        this.selected_item = (Product) adapter.getItemAtPosition(position);
    }

    public void btn_chooseitemClick(View view)
    {

        Intent returnIntent = new Intent();
        // get image
        //this.selected_item.setProductImage(ImageDownloader.getBitmapFromURL(this.selected_item.getProductImageUrl()));
        returnIntent.putExtra("result",this.selected_item.toBundle());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
