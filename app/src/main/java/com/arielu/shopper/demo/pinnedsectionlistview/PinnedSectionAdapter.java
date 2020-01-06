package com.arielu.shopper.demo.pinnedsectionlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.arielu.shopper.demo.R;
import com.arielu.shopper.demo.classes.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import androidx.core.content.ContextCompat;

public class PinnedSectionAdapter extends BaseExpandableListAdapter implements PinnedSectionListView.PinnedSection, AbsListView.OnScrollListener, Filterable {
    private Context context;
    private ArrayList<String> displayTitles;
    public TreeMap<Integer,List<Integer>> selectedItems;
    private TreeMap<String,ArrayList<Product>> displayData,data;
    private final int blue,white;

    public PinnedSectionAdapter(Context context, TreeMap<String, ArrayList<Product>> data) {
        this.context = context;
        this.displayTitles = (ArrayList<String>) createTitles(data.keySet());
        this.displayData = data;
        this.data = data;
        blue = ContextCompat.getColor(context,R.color.blue);
        white = ContextCompat.getColor(context, R.color.white);
        selectedItems=new TreeMap<>();
    }

    @Override
    public int getGroupCount() {
        return displayTitles.size();
    }

    @Override
    public int getChildrenCount(int i) {
        //return displayData.get(displayData.keySet().toArray()[i]).size();
//        if (!displayTitles.isEmpty()&&!displayData.containsKey(displayTitles.get(i)))
//            return -1;
        if (displayTitles.size()<=i)
            return 0;
        return displayData.get(displayTitles.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
//        return displayData.keySet().toArray()[i];
       return displayTitles.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return displayData.get(displayData.keySet().toArray()[i]).get(i1);
       // return displayData.get(displayTitles.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String title = (String) getGroup(i);
        if (view==null){
            LayoutInflater layoutInflater =(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_group,null);
        }
        TextView listTitleView = view.findViewById(R.id.group_title);
        listTitleView.setText(title);
        //((TextView)view.findViewById(R.id.group_title)).setText(displayTitles.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view==null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }
        Product currItem = (Product) getChild(i,i1);
        ((TextView)view.findViewById(R.id.item_name)).setText(currItem.getProductName());
        ((TextView)view.findViewById(R.id.item_price)).setText("\u20AA"+currItem.getProductPrice());
        ((ImageView)view.findViewById(R.id.item_image)).setImageBitmap(currItem.ProductImage());
        if (selectedItems.containsKey(i)&&selectedItems.get(i).contains(i1))
            view.setBackgroundColor(blue);
        else
            view.setBackgroundColor(white);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    public void configurePinnedSection(View v, int position, int alpha) {
        TextView header = v.findViewById(R.id.group_title);
        header.setAlpha(alpha);
        v.setAlpha(alpha);
        final String title = displayTitles.get(position);
        header.setText(title);
    }
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedSectionListView) {
            ((PinnedSectionListView) view).configureSectionTop(firstVisibleItem);
            ((PinnedSectionListView) view).configureSectionBottom(firstVisibleItem+visibleItemCount-1);
        }
    }
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }
    public void remove(){
        for (int group:selectedItems.keySet()) {
            String currGroup = displayTitles.get(group);
            ArrayList list = data.get(currGroup);
            Collections.sort(selectedItems.get(group),Collections.<Integer>reverseOrder());
            for (int child:selectedItems.get(group)) {
                if (list.size()<=1)
                    data.remove(currGroup);
                else
                    list.remove(child);
            }
        }
        selectedItems.clear();
        this.displayData = data;
        this.displayTitles = (ArrayList<String>) createTitles(data.keySet());
        notifyDataSetChanged();
    }
    public void add(){

    }

    public boolean select(int group, int child){
        ArrayList<Integer> list;
        if (selectedItems.containsKey(group)){
            list = (ArrayList<Integer>) selectedItems.get(group);
            if (list.contains(child)){
                if (list.size()==1)
                    selectedItems.remove(group);
                else
                    list.remove(child);
                return false;
            }else list.add(child);
        }else{
            list = new ArrayList<>();
            list.add(child);
            selectedItems.put(group,list);
        }
        return true;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                TreeMap<String,ArrayList<Product>> filterData = new TreeMap<>();
                int count=0;
                charSequence = charSequence.toString().toLowerCase();
                for (String group: data.keySet()) {
                    if (group.toLowerCase().startsWith(charSequence.toString())) {
                        filterData.put(group, data.get(group));
                        count+= data.get(group).size();
                    }
                    else
                        for (Product product: data.get(group)) {
                            if (product.getProductName().toLowerCase().startsWith(charSequence.toString())){
                                if (filterData.containsKey(group))
                                    filterData.get(group).add(product);
                                else {
                                    ArrayList list = new ArrayList();
                                    list.add(product);
                                    filterData.put(group, list);
                                }
                                count+=1;
                            }
                        }
                }
                results.count = count;
                results.values=filterData;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayData = (TreeMap<String, ArrayList<Product>>) filterResults.values;
                displayTitles = (ArrayList<String>) createTitles(displayData.keySet());
                notifyDataSetChanged();
            }
        };
        return filter;
    }
    private List<String> createTitles(Set<String> groupNames){
        ArrayList<String> list = new ArrayList<>();
        for (String groupName:groupNames) {
            list.add(groupName);
        }
        return list;
    }
    public void updateList(){
        displayData = data;
        displayTitles = (ArrayList<String>) createTitles(displayData.keySet());
        notifyDataSetChanged();
    }
}
