package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arielu.shopper.demo.classes.Branch;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.models.SessionProduct;
import com.arielu.shopper.demo.models.StoreProductRef;
import com.arielu.shopper.demo.utilities.ImageDownloader;
import com.arielu.shopper.demo.classes.Product;
import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.utilities.ObserverFirebaseTemplate;
import com.google.firebase.database.core.utilities.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserShoppingListActivity extends AppCompatActivity {

    private TreeMap<String, ArrayList<SessionProduct>> list = new TreeMap<>();
    private ExpandableListView ELV;
    private BaseExpandableListAdapter ELA;

    private String listID;
    private String listName;

    private Branch selectedBranch;


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


        Firebase2.getListItems(this.listID, (data) -> {
            List<SessionProduct> products = (List<SessionProduct>) data;
            ArrayList<SessionProduct> temp;
            for(SessionProduct p : products)
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

        });


    }

    private void expandAll()
    {
        for(int i = 0; i < list.size();i++)
        {
            ELV.expandGroup(i);
        }
    }

    private void getProductsPrice(Branch branch)
    {
        // create a subject (observable/observer) that notifies when a product retrieved from Firebase.
        PublishSubject<StoreProductRef> storeProdRefSubject = PublishSubject.create();
        // now declare what to do when the subject notifies us.
        storeProdRefSubject.subscribe((storeProductRef) -> {
            List<SessionProduct> products = convertProductsMapToList(this.list);

            SessionProduct prod = null;
            for (SessionProduct p : products)
            {
                if(p.getProductCode().equals(storeProductRef.getProductCode()))
                {
                    prod = p;
                    break;
                }
            }

            prod.setProductPrice(storeProductRef.getPrice());

            ELA.notifyDataSetChanged();
        });

        List<SessionProduct> products = convertProductsMapToList(this.list);

        for (SessionProduct p : products)
            Firebase.getStoreProductByCode(p.getProductCode(),branch.getCompany_id()+"-"+branch.getBranch_id(), storeProdRefSubject);

    }

    private List<SessionProduct> convertProductsMapToList(Map<String, ArrayList<SessionProduct>> productsMap)
    {
        Collection<ArrayList<SessionProduct>> c = productsMap.values();
        ArrayList<SessionProduct> lst = new ArrayList<>();
        for(ArrayList<SessionProduct> l : c ) { lst.addAll(l); }

       return lst;
    }


    public void btn_searchitemsClick(View view)
    {
        Intent intent = new Intent(this, SearchItemsActivity.class);
        startActivityForResult(intent,1);
    }

    public void btn_searchBranchesClick(View view)
    {
        Intent intent = new Intent(this, BranchesActivity.class);
        startActivityForResult(intent,2);
    }


    public void btn_saveClick(View view)
    {
        List<SessionProduct> lst = convertProductsMapToList(list);
        Firebase2.setListProducts(listID,lst);

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

        switch (requestCode) {
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    // parse result into a Product.class (serializable).
                    Product result= Product.fromBundle(data.getParcelableExtra("result"));
                    // create a SessionProduct from Product using copy-constructor
                    SessionProduct sessProd = new SessionProduct(result);
                    // download & set bitmap image to the product.
                    result.setProductImage(ImageDownloader.getBitmapFromURL(result.getProductImageUrl()));

                    // find correct group, if the group does not exists yet, create it.
                    ArrayList<SessionProduct> temp;
                    Boolean isCategoryExistInView = (list.get(result.getCategoryName()) == null);
                    if(isCategoryExistInView) {
                        temp = new ArrayList<>();
                        list.put(result.getCategoryName(), temp);

                    } else {
                        temp = list.get(result.getCategoryName());
                    }

                    // add the product to the group, dont allow duplicates.
                    if(!temp.contains(result))
                        temp.add(sessProd);

                    // notify adapter that changes were made to the dataset.
                    ELA.notifyDataSetChanged();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
                ///////////////////////////////////////////////
            case 2:
                if(resultCode == Activity.RESULT_OK) {
                    selectedBranch = (Branch) data.getSerializableExtra("result");
                    getProductsPrice(selectedBranch);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
                ///////////////////////////////////////////////
        }
    }//onActivityResult
}



   