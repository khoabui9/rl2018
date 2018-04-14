package com.site.app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import android.support.v7.app.AlertDialog;
import com.site.app.models.Site;
import static java.lang.Math.toIntExact;


import java.util.ArrayList;
import java.util.List;

public class SiteList extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 100;
    static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    ListView siteList;
    DatabaseHelper db;
    SiteAdapter adapter;
    ArrayList<Site> listSite = new ArrayList<>();
    Dialog dialog;
    ImageView mImageView;

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




        // /search button
        Button searchbtn = (Button) findViewById(R.id.search_btn);

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
                        else {
                            loadSiteList();
                            adapter.filter(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.project_create_dialog);

        final Button createprojectbtn = (Button) dialog.findViewById(R.id.create_btn);
        final Button ccancelbtn = (Button) dialog.findViewById(R.id.cancel_btn);
        final EditText name = (EditText) dialog.findViewById(R.id.project_name_input);

        //Creat new project button
        Button addnewbtn = (Button) findViewById(R.id.add_btn);

        //showing input dialog after clicked
        addnewbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.show();

            }
        });
        createprojectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = name.getText().toString();
                db.addData(input);
                loadSiteList();
                name.setText("");
                dialog.dismiss();
            }
        });

        ccancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        siteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
//
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
                Intent i = new Intent(SiteList.this, FormEditting.class);
                int a = (int) id;
                Site site = listSite.get(a);
                i.putExtra("id", site.getId());
                String aa = site.getName() + site.getId();
                Toast.makeText(SiteList.this, aa, Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
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
    
    //Top Corner Menu:
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(SiteList.this, MainActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
