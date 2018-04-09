package com.site.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class FormEditting extends AppCompatActivity {

    private TextView projectNameLabel, workSiteLable, attendantLabel, dateTimeLabel;
    private EditText projectNameInp, workSiteInp, attendantInp, dateTimeInp;
    private Button backBtn, doneBtn, saveBtn, shareBtn, addImageBtn;
    private ListView selectedImagesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_editting);

        //Back button to previous page
        backBtn = (Button) findViewById(R.id.back_btn);

        //Done button of Done edditing
        doneBtn = (Button) findViewById(R.id.done_edit_btn);
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
        selectedImagesList = (ListView) findViewById(R.id.selectedimages_list);

        //Add more images button
        addImageBtn = (Button) findViewById(R.id.add_img_btn);

        //Save button
        saveBtn  = (Button) findViewById(R.id.save_btn);

        //Share button
        shareBtn = (Button) findViewById(R.id.share_btn);


    };
}
