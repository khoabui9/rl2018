package com.site.app;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class FormEditting extends AppCompatActivity {



    private TextView projectNameLabel, workSiteLable, attendantLabel, dateTimeLabel, problemsLabel, measuresLabel;
    private EditText projectNameInp, workSiteInp, attendantInp, dateTimeInp, problemInp, measureInp;

    private Button backBtn, doneBtn, saveBtn, shareBtn, addImageBtn;
    private GridView selectedImagesList;
    private ProgressBar mProgressbar;
    private TextView textViewProgress;

    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9;

    private String stringProb1 = "", stringProb2 = "", stringProb3 = "";
    private String stringProb4 = "", stringProb5 = "", stringProb6 = "";
    private String stringProb7 = "", stringProb8 = "", stringProb9 = "";
    private String stringWorkSite = "", stringProjectName = "Unspecified!";

//    private FirebaseAuth mAuth;

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    java.util.List<String> imagesEncodedList;

    private  ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private  ArrayList<String> mArrayFilePath = new ArrayList<String>();
    private  ArrayList<String> mArrayProblems = new ArrayList<String>();
    private ArrayList<ImageView> mArrayImage = new ArrayList<ImageView>();


    //private Button backBtn, doneBtn, saveBtn, shareBtn, addImageBtn, goToEditImage;

    private int id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_editting);
        Intent i = getIntent();
        id = i.getIntExtra("id", 0);

        //ImageViews
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);
        imageView8 = (ImageView) findViewById(R.id.imageView8);
        imageView9 = (ImageView) findViewById(R.id.imageView9);

        mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
        textViewProgress = (TextView) findViewById(R.id.textViewProgress);

        //Back button to previous page
        backBtn = (Button) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormEditting.this, FullscreenActivity.class);
                startActivity(intent);
            }
        });

        //Done button of Done edditing
//        doneBtn = (Button) findViewById(R
//                .id.done_edit_btn);
//        doneBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(FormEditting.this, SiteList.class);
//                startActivity(i);
//            }
//        });

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
        //input of measures
        projectNameLabel = (TextView) findViewById(R.id.measures);
        //projectNameInp = (EditText) findViewById(R.id.measures_input);

     //SHOWING LIST OF IMAGES
        selectedImagesList = (GridView) findViewById(R.id.selectedimages_list);



        //Add more images button
        addImageBtn = (Button) findViewById(R.id.add_img_btn);

        //Save button
//        saveBtn  = (Button) findViewById(R.id.save_btn);

        //Share button
        shareBtn = (Button) findViewById(R.id.share_btn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.docs");
                startActivity(intent);

            }
        });

        //Initial Firebase
//        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Problems editText:
        final EditText editText1 = (EditText) findViewById(R.id.editText1);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        final EditText editText3 = (EditText) findViewById(R.id.editText3);
        final EditText editText4 = (EditText) findViewById(R.id.editText4);
        final EditText editText5 = (EditText) findViewById(R.id.editText5);
        final EditText editText6 = (EditText) findViewById(R.id.editText6);
        final EditText editText7 = (EditText) findViewById(R.id.editText7);
        final EditText editText8 = (EditText) findViewById(R.id.editText8);
        final EditText editText9 = (EditText) findViewById(R.id.editText9);

        String userEmailString = "";

//        FirebaseUser firebaseUser = auth.getCurrentUser();
//        if (firebaseUser != null) {
//
//            String userEmail = firebaseUser.getEmail();
//            userEmailString = userEmail.replace("@gmail.com", "");
//
//        }

        //Set the attendant name:
        attendantInp.setText(userEmailString);

        //set the date and time:
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        dateTimeInp.setText(date.toString());

        //Add images from gallery
        final int RESULT_LOAD_IMG = 1;
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        //PDF Create
        Button pdf_preview_button = (Button) findViewById(R.id.pdf_preview);

        pdf_preview_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mProgressbar.setVisibility(View.VISIBLE);
                textViewProgress.setVisibility(View.VISIBLE);

                stringProb1 = editText1.getText().toString();
                stringProb2 = editText2.getText().toString();
                stringProb3 = editText3.getText().toString();
                stringProb4 = editText4.getText().toString();
                stringProb5 = editText5.getText().toString();
                stringProb6 = editText6.getText().toString();
                stringProb7 = editText7.getText().toString();
                stringProb8 = editText8.getText().toString();
                stringProb9 = editText9.getText().toString();

                stringProjectName = projectNameInp.getText().toString();
                stringWorkSite = workSiteInp.getText().toString();

                mArrayProblems.add(stringProb1);
                mArrayProblems.add(stringProb2);
                mArrayProblems.add(stringProb3);
                mArrayProblems.add(stringProb4);
                mArrayProblems.add(stringProb5);
                mArrayProblems.add(stringProb6);
                mArrayProblems.add(stringProb7);
                mArrayProblems.add(stringProb8);
                mArrayProblems.add(stringProb9);

                Document document = new Document();
                try {

                    //String filePath = getApplicationContext().getFilesDir().getPath().toString() + "/fileName.pdf";
//                    File f = new File(filePath);

                    File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PdfFolder");

                    if (!pdfFile.exists()) {
                        if (!pdfFile.mkdir()) {
                            Toast.makeText(FormEditting.this, "failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

//                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
//                    String date = df.format(Calendar.getInstance().getTime());
//                    dateTimeInp.setText(date.toString());

//                    File pdf = new File(pdfFile.getPath() + File.separator + "firstPdf.pdf");
                    File pdf = new File(pdfFile.getPath() + File.separator + stringProjectName + ".pdf");

                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdf.getPath()));
                    document.open();
//                    document.add(new Paragraph("Inspection PDF:"));
//                    document.add(new Paragraph("Inspector:" + attendantInp.getText().toString()));
//                    document.add(new Paragraph("Date and time: " + dateTimeInp.getText().toString()));
//                    document.add(new Paragraph("Project name: " + stringProjectName));
//                    document.add(new Paragraph("Worksite/Address: " + stringWorkSite));

//                    PdfPTable table = new PdfPTable(3);
//                    table.setWidthPercentage(105);
//                    table.setSpacingBefore(11f);
//                    table.setSpacingAfter(11f);

                    try {

//                        Bitmap bmp = BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/IMG_20180411_094332.jpg");
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        Image image = Image.getInstance(stream.toByteArray());
//                        document.add(image);


                    } catch (Exception e) {
                        Log.v("LOG_TAG", "Image CAN NOT ADDED" + e);

                    }

                    try {
                        // get input stream
                        InputStream ims = getAssets().open("logo.JPG");
                        Bitmap bmp = BitmapFactory.decodeStream(ims);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);
                        Image image = Image.getInstance(stream.toByteArray());
                        document.add(image);
                        //Log.v("LOG_TAG", "Image addeddddddddddddddddddddd");
                    } catch (IOException ex) {
                        return;
                    }

                    document.add(new Paragraph("Inspection PDF:"));
                    document.add(new Paragraph("Inspector:" + attendantInp.getText().toString()));
                    document.add(new Paragraph("Date and time: " + dateTimeInp.getText().toString()));
                    document.add(new Paragraph("Project name: " + stringProjectName));
                    document.add(new Paragraph("Worksite/Address: " + stringWorkSite));

                    Log.v("LOG_TAG", "mArray File Path: " + mArrayFilePath.size());
                    //Log.v("LOG_TAG", "mArray Image: " + mArrayImage.size());

                    for(int i = 0; i < mArrayFilePath.size(); i++) {

                    //int i = 0;

                    try {

                        document.add(new Paragraph("."));
                        document.add(new Paragraph("."));
                        document.add(new Paragraph("."));
                        document.add(new Paragraph("."));
                        document.add(new Paragraph("."));
                        document.add(new Paragraph("Problem " + (i + 1) + " description : " + mArrayProblems.get(i)));

                        Bitmap bmp = BitmapFactory.decodeFile(mArrayFilePath.get(i));
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);
                        Image image = Image.getInstance(stream.toByteArray());
                        image.scaleAbsolute(350, 450);
                        document.add(image);
                        document.add(new Paragraph("."));
                        document.add(new Paragraph("."));
                        document.add(new Paragraph("."));

//                        if (mArrayProblems.get(i) != "") {
//                            document.add(new Paragraph("Problem " + (i + 1) + " description : " + mArrayProblems.get(i)));
//                        }

                    } catch (IOException e) {

                    }

                }

                    document.close();
                    writer.close();
                    Toast.makeText(FormEditting.this, "The file created in " + pdf + "!" , Toast.LENGTH_LONG).show();
                    mProgressbar.setVisibility(View.INVISIBLE);
                    textViewProgress.setVisibility(View.INVISIBLE);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(FormEditting.this, "File exception error!", Toast.LENGTH_SHORT).show();
                    mProgressbar.setVisibility(View.INVISIBLE);
                    textViewProgress.setVisibility(View.INVISIBLE);
                } catch (DocumentException e) {
                    e.printStackTrace();
                    Toast.makeText(FormEditting.this, "Document exception error!", Toast.LENGTH_SHORT).show();
                    mProgressbar.setVisibility(View.INVISIBLE);
                    textViewProgress.setVisibility(View.INVISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    mProgressbar.setVisibility(View.INVISIBLE);
                    textViewProgress.setVisibility(View.INVISIBLE);
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mArrayFilePath.clear();

        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    mArrayUri.add(mImageUri);



//                    mArrayImage.add(imageView1);
//                    mArrayImage.add(imageView2);
//                    mArrayImage.add(imageView3);
//                    mArrayImage.add(imageView4);
//                    mArrayImage.add(imageView5);
//                    mArrayImage.add(imageView6);
//                    mArrayImage.add(imageView7);
//                    mArrayImage.add(imageView8);
//                    mArrayImage.add(imageView9);
//
//                    int i;
//                    for (i = 0 ; i < mArrayUri.size() ; i++) {
////                    Log.v("LOG_TAG", "Selected Images: " + mArrayUri.get(i));
//                        String mArrayString = String.valueOf(mArrayUri.get(i));
////                    Log.v("LOG_TAG", "Selected Images String: " + mArrayString);
//
////                    File myFile = new File(mArrayUri.get(i).toString());
//
//                        String realPath;
//
//                        realPath = RealPathUtil.getRealPathFromURI_API19(this, mArrayUri.get(i));
//
//                        mArrayFilePath.add(realPath);
//
////                    String convertedPath = getRealPathFromURI(mArrayUri.get(i));
//////
////                    Uri uriFromPath = Uri.fromFile(new File(convertedPath));
////
////                    Log.v("LOG_TAG", "new method test: " + uriFromPath.toString());
//
//                        Log.v("LOG_TAG", "Selected Images Stringggggggggggggggggg: " + realPath);
//
////                            Bitmap myBitmap = BitmapFactory.decodeFile(realPath);
////
////                            imageView4.setImageBitmap(myBitmap);
//
////                    Log.v("LOG_TAG", "Selected Absolute Path: " + getRealPathFromURI(mArrayUri.get(i)));
//
//
//                        final InputStream imageStream = getContentResolver().openInputStream(mArrayUri.get(i));
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//
//                        if(mArrayImage.get(i).getDrawable() == null) {
//                            mArrayImage.get(i).setImageBitmap(selectedImage);
//
//                        }
//                        else {
//                            //Log.v("LOG_TAG", "the first free one is " + i);
//                        }
//                    }




                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        //mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            //Getting file Path
                            String path = mArrayUri.get(i).getPath();

//                            getRealPathFromURI(uri);

                            Log.v("LOG_TAG", "Selected Images Path: " + path);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());

                    }
                }

                mArrayImage.add(imageView1);
                mArrayImage.add(imageView2);
                mArrayImage.add(imageView3);
                mArrayImage.add(imageView4);
                mArrayImage.add(imageView5);
                mArrayImage.add(imageView6);
                mArrayImage.add(imageView7);
                mArrayImage.add(imageView8);
                mArrayImage.add(imageView9);

                int i;
                for (i = 0 ; i < mArrayUri.size() ; i++) {
//                    Log.v("LOG_TAG", "Selected Images: " + mArrayUri.get(i));
                    String mArrayString = String.valueOf(mArrayUri.get(i));
//                    Log.v("LOG_TAG", "Selected Images String: " + mArrayString);

//                    File myFile = new File(mArrayUri.get(i).toString());

                    String realPath = "";

                    realPath = RealPathUtil.getRealPathFromURI_API19(this, mArrayUri.get(i));

                    mArrayFilePath.add(realPath);

//                    String convertedPath = getRealPathFromURI(mArrayUri.get(i));
////
//                    Uri uriFromPath = Uri.fromFile(new File(convertedPath));
//
//                    Log.v("LOG_TAG", "new method test: " + uriFromPath.toString());

                    Log.v("LOG_TAG", "Selected Images Stringggggggggggggggggg: " + realPath);

//                            Bitmap myBitmap = BitmapFactory.decodeFile(realPath);
//
//                            imageView4.setImageBitmap(myBitmap);

//                    Log.v("LOG_TAG", "Selected Absolute Path: " + getRealPathFromURI(mArrayUri.get(i)));


                    final InputStream imageStream = getContentResolver().openInputStream(mArrayUri.get(i));
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    if(mArrayImage.get(i).getDrawable() == null) {
                        mArrayImage.get(i).setImageBitmap(selectedImage);

                    }
                    else {
                        //Log.v("LOG_TAG", "the first free one is " + i);
                    }
                }



            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



//    public String getRealPathFromURI(Uri contentUri)
//    {
//        String[] proj = { MediaStore.Audio.Media.DATA };
//        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }


//    public String getRealPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//
//        //This method was deprecated in API level 11
//        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//
//        CursorLoader cursorLoader = new CursorLoader(
//                this,
//                contentUri, proj, null, null, null);
//        Cursor cursor = cursorLoader.loadInBackground();
//
//        int column_index =
//                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }



//    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent (FormEditting.this, FullscreenActivity.class);
        startActivity(intent);
    }

}



/*
    @Override
    public void onBackPressed() {
        FormEditting.this.finish();
    }
}*/


