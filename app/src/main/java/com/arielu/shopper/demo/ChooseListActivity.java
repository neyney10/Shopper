package com.arielu.shopper.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.arielu.shopper.demo.classes.Shopping_list;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.fragments.CollectionPagerAdapter;
import com.arielu.shopper.demo.fragments.UserListsFragment;
import com.arielu.shopper.demo.fragments.UserListsSharedFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChooseListActivity extends AppCompatActivity implements DialogAddList.DialogListener
{
    public Toolbar toolbar;
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private ProgressBar progressBar;
    public boolean isSelectOn;
    public int blue,white;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        //ViewPager
        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(collectionPagerAdapter);
        tabLayout = findViewById(R.id.lists_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Test", "onPageSelected: "+position);
                if (isSelectOn&&position!=0)
                    cancel();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List's");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.spinner_loader);
        blue = ContextCompat.getColor(getApplicationContext(),R.color.blue);
        white = ContextCompat.getColor(getApplicationContext(), R.color.white);
    }
    public void createNewList(View v){
        DialogFragment newFragment = new DialogAddList();
        newFragment.show(getSupportFragmentManager(), "Add List");

    }

    //delegate between dialog and activity
    @Override
    public void addList(String listName) {
        ((UserListsFragment)currentFragmentDisplay()).addList(listName);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isSelectOn)
            getMenuInflater().inflate(R.menu.select_mode_menu,menu);
        else {
            getMenuInflater().inflate(R.menu.lists_menu, menu);
            MenuItem searchItem = menu.findItem(R.id.search_filter);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Search...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    switch (viewPager.getCurrentItem()){
                        case 0:
                            ((UserListsFragment)currentFragmentDisplay()).getFilter(newText);
                            break;
                        case 1:
                            ((UserListsSharedFragment)currentFragmentDisplay()).getFilter(newText);
                            break;
                        default:
                    }
                    return false;
                }
            });
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    tabLayout.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    tabLayout.setVisibility(View.VISIBLE);
                    return true;
                }
            });
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                remove();
                return true;
            case R.id.search_filter:

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSelectOn)
            cancel();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void cancel(){
        switch (viewPager.getCurrentItem()){
            case 0:
            case 1:
                ((UserListsFragment)collectionPagerAdapter.getItem(0)).cancel();
                break;
                default:
                    return;
        }
    }
    private void remove(){
        switch (viewPager.getCurrentItem()){
            case 0:
                ((UserListsFragment)currentFragmentDisplay()).remove();
                selectMode(false);
                break;
                default: return;
        }
    }
    private void setSearchView(){

    }
    public void selectMode(boolean state){
        isSelectOn=state;
        if (state) {
            ((UserListsFragment)collectionPagerAdapter.getItem(0)).FAB.setVisibility(View.GONE);
        }
        else {
            toolbar.setTitle("List's");
            ((UserListsFragment)collectionPagerAdapter.getItem(0)).FAB.setVisibility(View.VISIBLE);
        }
        invalidateOptionsMenu();
    }
    private Fragment currentFragmentDisplay(){
        return collectionPagerAdapter.getItem(viewPager.getCurrentItem());
    }
}
