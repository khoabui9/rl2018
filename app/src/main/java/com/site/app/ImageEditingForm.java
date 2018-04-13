package com.site.app;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageEditingForm extends AppCompatActivity {
    private TextView  problemsLabel, fmeasureLable, tmeasureLable;
    private EditText  problemInp, fmeasureInp, tmeasureInp;
    private Button backBtnImge, saveDoneBtnImge;
    private ImageView imgPrev;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_images_list);

        //Back button to previous page
        backBtnImge = (Button) findViewById(R.id.back_btn_imge);
        backBtnImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ImageEditingForm.this, FormEditting.class);
                startActivity(i);
            }
        });

        //Done button of Done edditing
        saveDoneBtnImge = (Button) findViewById(R
                .id.done_save_btn_imge);
        saveDoneBtnImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ImageEditingForm.this, FormEditting.class);
                startActivity(i);
            }
        });

        //Image Preview

        imgPrev = (ImageView) findViewById(R.id.imgprev);

        //Problems input
        problemsLabel = (TextView) findViewById(R.id.problems_label);
        problemInp = (EditText) findViewById(R.id.problems_input);

        //Taken measures input
        tmeasureLable = (TextView) findViewById(R.id.taken_measures);
        tmeasureInp = (EditText) findViewById(R.id.takenmeasures_input);

        //Further measures input
        fmeasureLable = (TextView) findViewById(R.id.further_measures);
        fmeasureInp = (EditText) findViewById(R.id.furthermeasure_input);
    }
}
