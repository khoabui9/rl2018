package com.site.app;

import android.app.Activity;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FormEditting extends AppCompatActivity {

    private TextView projectNameLabel, workSiteLable, attendantLabel, dateTimeLabel, problemsLabel, fmeasureLable, tmeasureLable;
    private EditText projectNameInp, workSiteInp, attendantInp, dateTimeInp, problemInp, fmeasureInp, tmeasureInp;
    private Button backBtn, doneBtn, saveBtn, shareBtn, addImageBtn;
    private ListView selectedImagesList;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9;

    private FirebaseAuth mAuth;

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    java.util.List<String> imagesEncodedList;

    private  ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_editting);

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



        //Back button to previous page
        backBtn = (Button) findViewById(R.id.back_btn);

        //Done button of Done edditing
        doneBtn = (Button) findViewById(R
                .id.done_edit_btn);
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

        //Problems input
        problemsLabel = (TextView) findViewById(R.id.problems_label);
        problemInp = (EditText) findViewById(R.id.problems_input);

        //Taken measures input
        tmeasureLable = (TextView) findViewById(R.id.taken_measures);
        tmeasureInp = (EditText) findViewById(R.id.takenmeasures_input);

        //Further measures input
        fmeasureLable = (TextView) findViewById(R.id.further_measures);
        fmeasureInp = (EditText) findViewById(R.id.furthermeasure_input);


        //Add more images button
        addImageBtn = (Button) findViewById(R.id.add_img_btn);

        //Save button
        saveBtn  = (Button) findViewById(R.id.save_btn);

        //Share button
        shareBtn = (Button) findViewById(R.id.share_btn);

        //Initial Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Problems editText:
        final EditText editText1 = (EditText) findViewById(R.id.editText1);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        final EditText editText3 = (EditText) findViewById(R.id.editText3);
        EditText editText4 = (EditText) findViewById(R.id.editText4);

        String userEmailString = "unknown";

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {

            String userEmail = firebaseUser.getEmail();
            userEmailString = userEmail.replace("@gmail.com", "");

        }

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
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

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

                Document document = new Document();
                try {

                    //String filePath = getApplicationContext().getFilesDir().getPath().toString() + "/fileName.pdf";
//                    File f = new File(filePath);

                    File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyFirstCameraApp");

                    if (!pdfFile.exists()) {
                        if (!pdfFile.mkdir()) {
                            Toast.makeText(FormEditting.this, "failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    File pdf = new File(pdfFile.getPath() + File.separator + "firstPdf.pdf");

                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdf.getPath()));
                    document.open();
                    document.add(new Paragraph("Inspection PDF:"));
                    document.add(new Paragraph("Inspector:" + attendantInp.getText().toString()));
                    document.add(new Paragraph("Data and time: " + dateTimeInp.getText().toString()));

                    PdfPTable table = new PdfPTable(3);
                    table.setWidthPercentage(105);
                    table.setSpacingBefore(11f);
                    table.setSpacingAfter(11f);

//                    mArrayUri = new ArrayList<Uri>();

                    Log.v("LOG_TAG", "Selected Images First Path: " + mArrayUri.get(1).getPath());

                    try {
                        //final InputStream imageStream = getContentResolver().openInputStream(Uri.parse(mArrayUri.get(1).getPath()));
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        Image image = Image.getInstance(stream.toByteArray());
//                        document.add(image);

                        File file = new File(mArrayUri.get(1).getPath());

                        String path = (mArrayUri.get(1)).getEncodedPath();



                        Image img = Image.getInstance(file.getAbsolutePath());
                        document.setPageSize(img);
                        document.newPage();
                        img.setAbsolutePosition(0, 0);
                        document.add(img);


                        Log.v("LOG_TAG", "Image added finally");

//                    Image img = Image.getInstance(String.valueOf(mArrayUri.get(0)));
//                    document.setPageSize(img);
//                    document.newPage();
//                    img.setAbsolutePosition(0, 0);
//                    document.add(img);

                    } catch(Exception e) {
                        Log.v("LOG_TAG", "Image CAN NOT ADDED" + e);

                    }

                    try {
                        // get input stream
                        InputStream ims = getAssets().open("logo.JPG");
                        Bitmap bmp = BitmapFactory.decodeStream(ims);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Image image = Image.getInstance(stream.toByteArray());
                        document.add(image);
                        Log.v("LOG_TAG", "Image addeddddddddddddddddddddd");
                    }
                    catch(IOException ex)
                    {
                        return;
                    }

//                    Image img = Image.getInstance(String.valueOf(mArrayUri.get(1)));
//                    img.scaleAbsolute(400, 300);
//                    img.setAbsolutePosition(0, 0);
//                    PdfImage stream = new PdfImage(img, "", null);
//                    stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
//                    PdfIndirectObject ref = writer.addToBody(stream);
//                    img.setDirectReference(ref.getIndirectReference());
//                    document.add(img);

                    float[] colWidth = {2f, 2f, 2f};
                    table.setWidths(colWidth);
                    PdfPCell c1 = new PdfPCell(new Paragraph("Problem1"));
                    PdfPCell c2 = new PdfPCell(new Paragraph("Probelm2"));
                    PdfPCell c3 = new PdfPCell(new Paragraph("Problem3"));
                    table.addCell(c1);
                    table.addCell(c2);
                    table.addCell(c3);
                    document.add(table);

                    List orderList = new List(List.ORDERED);
                    orderList.add(new ListItem("Problem1: " + editText1.getText().toString()));
                    orderList.add(new ListItem("Problem2" + editText2.getText().toString()));
                    orderList.add(new ListItem("Problem3" + editText3.getText().toString()));
                    document.add(orderList);

                    List unOrderList = new List(List.UNORDERED);
                    unOrderList.add(new ListItem("Problem4"));
                    unOrderList.add(new ListItem("Problem5"));
                    unOrderList.add(new ListItem("Problem6"));
                    document.add(unOrderList);

                    document.close();
                    writer.close();
                    Toast.makeText(FormEditting.this, "The file created!", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(FormEditting.this, "File exception error!", Toast.LENGTH_SHORT).show();

                } catch (DocumentException e) {
                    e.printStackTrace();
                    Toast.makeText(FormEditting.this, "Document exception error!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

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
//                        mArrayUri = new ArrayList<Uri>();
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

                        ArrayList<ImageView> mArrayImage = new ArrayList<ImageView>();
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
                            Log.v("LOG_TAG", "Selected Images: " + mArrayUri.get(i));
                            String mArrayString = String.valueOf(mArrayUri.get(i));
                            Log.v("LOG_TAG", "Selected Images String: " + mArrayString);

                            File myFile = new File(mArrayUri.get(i).toString());
//                            myFile.getAbsolutePath();

//                            String path = mArrayUri.get(i).getPath();
//                            Log.v("LOG_TAG", "Selected Images Path: " + path);

//                                String something3 = mArrayString.replace("content:", "");
//
//                                Uri someThing4 = Uri.parse(something3);
//





//
//                            String someThing = getRealPathFromURI(someThing4);
////
////                            String someThing = someThing1.replace("content:", "");
////
                            Log.v("LOG_TAG", "Selected Absolute Path: " + getRealPathFromURI(mArrayUri.get(i)));


                            final InputStream imageStream = getContentResolver().openInputStream(mArrayUri.get(i));
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                            if(mArrayImage.get(i).getDrawable() == null) {
                                mArrayImage.get(i).setImageBitmap(selectedImage);

                            }
                            else {
                                //Log.v("LOG_TAG", "the first free one is " + i);
                            }
                        }
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

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
