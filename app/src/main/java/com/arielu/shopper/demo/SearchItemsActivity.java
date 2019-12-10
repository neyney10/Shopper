package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.models.Product;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;

import java.util.ArrayList;

public class SearchItemsActivity extends AppCompatActivity {


    private ArrayList<Product> products;
    private ArrayList<Product> products_filtered;
    private ArrayAdapter<Product> adapter;
    private Product selected_item;

    // UI elements //
    private ListView lv_products_filtered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        products = new ArrayList<>(2);
        products_filtered = products = new ArrayList<>(30);

        LinkUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Observable<ArrayList<Product>> o = Firebase.getProductList();
        o.subscribe(new ObserverFirebaseTemplate<ArrayList<Product>>() {
            @Override
            public void onNext(ArrayList<Product> prods) {
                products = prods;
            }
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
        AutoCompleteTextView search_text = findViewById(R.id.tv_searchbox);
        search_text.setThreshold(2);

        ArrayAdapter<Product> adapter_hints_search = new ArrayAdapter<Product>(SearchItemsActivity.this,R.layout.item_test_sample,R.id.sample_text_view,products);
        search_text.setAdapter(adapter_hints_search);

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //?
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //?
            }

            @Override
            public void afterTextChanged(Editable editable) {

                products_filtered.clear();
                for(Product p : products)
                {

                    if(p.getItemName().contains(search_text.getText().toString()))
                        products_filtered.add(p);
                }
                adapter.notifyDataSetChanged();
            }


        });


    }

    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
        this.selected_item = (Product) adapter.getItemAtPosition(position);
    }

    public void btn_chooseitemClick(View view)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",this.selected_item);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
