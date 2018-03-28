package com.site.app;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.site.app.models.Site;

import java.util.ArrayList;
import java.util.List;

public class SiteList extends AppCompatActivity {
    ListView siteList;
    DatabaseHelper db;
    SiteAdapter adapter;
    ArrayList<Site> listSite = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list);

        db = new DatabaseHelper(this);
        db.cleardb();
        //Site site1 = new Site();
        //Site site2 = new Site();
        db.addData("site1");
        db.addData("site2");
        db.addData("site3");

        EditText edtxt = (EditText)findViewById(R.id.search_input);
        siteList = (ListView) findViewById(R.id.listViewLayout);
        loadSiteList();
        edtxt.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Toast.makeText(SiteList.this, s, Toast.LENGTH_SHORT).show();
                        if (s.toString() == null || s.toString().length() == 0 || s == "")
                            loadSiteList();
                        else
                            adapter.filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );
    }

    private  void loadSiteList() {
        Cursor data = db.getListContents();
        Site site = null;
        listSite = new ArrayList<>();
        while(data.moveToNext()){
            int id = data.getInt(0);
            String name = data.getString(1);
            site = new Site(name, id);
            listSite.add(site);
            adapter = new SiteAdapter(this, listSite);
            siteList.setAdapter(adapter);
        }
    }
}
