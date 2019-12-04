package com.arielu.shopper.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {
private Item[] items;
private Context context;

    public ItemAdapter(Item[] items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
        Item currItem = items[i];
        ((TextView)view.findViewById(R.id.item_name)).setText(currItem.getName());
        ((TextView)view.findViewById(R.id.item_price)).setText("\u20AA"+currItem.getPrice());
        return view;
    }
}
