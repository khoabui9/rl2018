package com.site.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.site.app.models.Site;

import java.util.ArrayList;

public class SiteAdapter  extends  BaseAdapter {
    Context c;
    ArrayList<Site> sites;
    LayoutInflater inflater;

    public SiteAdapter(Context c, ArrayList<Site> sites) {
        this.c = c;
        this.sites = sites;
    }

    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public Object getItem(int position) {
        return sites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null)
        {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.listview_item,parent,false);
        }

        TextView nameTxt= (TextView) convertView.findViewById(R.id.nameTxt);
        nameTxt.setText(sites.get(position).getName());

        final int pos=position;



        return convertView;
    }

    public void filter(String filter) {
        ArrayList<Site> filtered = new ArrayList<>();

        for (Site s : sites) {
            String name = s.getName();
            if (name.contains(filter)) {
                filtered.add(s);
            }
        }

        this.sites = filtered;

        notifyDataSetChanged();
    }
}
