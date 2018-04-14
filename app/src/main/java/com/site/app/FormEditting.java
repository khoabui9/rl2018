package com.site.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FormEditting extends AppCompatActivity {

    private TextView projectNameLabel, workSiteLable, attendantLabel, dateTimeLabel, problemsLabel, fmeasureLable, tmeasureLable;
    private EditText projectNameInp, workSiteInp, attendantInp, dateTimeInp, problemInp, fmeasureInp, tmeasureInp;
    private Button backBtn, doneBtn, saveBtn, shareBtn, addImageBtn, goToEditImage;
    private GridView selectedImagesList;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_editting);
        Intent i = getIntent();
        id = i.getIntExtra("id", 0);

        //Back button to previous page
        backBtn = (Button) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FormEditting.this, SiteList.class);
                startActivity(i);
            }
        });

        //Done button of Done edditing
        doneBtn = (Button) findViewById(R
                .id.done_edit_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FormEditting.this, SiteList.class);
                startActivity(i);
            }
        });

     //FORM
        //input of project name
        projectNameLabel = (TextView) findViewById(R.id.projectname_label);
        projectNameInp = (EditText) findViewById(R.id.projectname_input);

        //input of worksite
        workSiteLable = (TextView) findViewById(R.id.worksite);
        workSiteInp = (EditText) findViewById(R.id.worksite_input);

        //input of Attendants
        attendantLabel = (TextView) findViewById(R.id.attendant);
        attendantInp = (EditText) findViewById(R.id.attendant_input);

        //input of Date and Time
        dateTimeLabel = (TextView) findViewById(R.id.datetime);
        dateTimeInp = (EditText) findViewById(R.id.datetime_input);

     //SHOWING LIST OF IMAGES
        selectedImagesList = (GridView) findViewById(R.id.selectedimages_list);

        //This is just for testing UI
        goToEditImage = (Button) findViewById(R.id.testbtn);
        goToEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FormEditting.this, ImageEditingForm.class);
                startActivity(i);
            }
        });




        //Add more images button
        addImageBtn = (Button) findViewById(R.id.add_img_btn);

        //Save button
        saveBtn  = (Button) findViewById(R.id.save_btn);

        //Share button
        shareBtn = (Button) findViewById(R.id.share_btn);


    };

    @Override
    public void onBackPressed() {
        FormEditting.this.finish();
    }
}

