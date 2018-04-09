package com.site.app;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.Manifest;
import android.provider.MediaStore.Files.FileColumns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    public static final String TAG = "APP";
    private Camera mCamera;
    private CameraPreview mPreview;
    private int cameraId = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        if (checkCameraHardware(this)) {
            try {
                cameraId = findFrontFacingCamera();
                if (cameraId < 0) {
                } else {
                    mCamera = Camera.open(cameraId);
                }
                // Create our Preview view and set it as the content of our activity.
                mPreview = new CameraPreview(FullscreenActivity.this, mCamera);
                FrameLayout preview = (FrameLayout) findViewById(R.id.cam);
                preview.addView(mPreview);
            }
            catch (Exception e) {
                Toast.makeText(FullscreenActivity.this, "open camera failed." + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }


        Button next = (Button) findViewById(R.id.btn_next);
        Button capture = (Button) findViewById(R.id.btn_capture);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FullscreenActivity.this, SiteList.class);
                startActivity(i);
            }
        });



        capture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }
            try {
                Uri url = addImageToGallery(FullscreenActivity.this.getContentResolver(), "jpg", pictureFile.getAbsoluteFile());
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Log.v(TAG, "will now release camera");
                mCamera.release();
                Log.v(TAG, "will now call finish()");
                finish();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    public Uri addImageToGallery(ContentResolver cr, String imgType, File filepath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "player");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "player");
        values.put(MediaStore.Images.Media.DESCRIPTION, "");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + imgType);
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATA, filepath.toString());

        return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, mediaFile.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        } else {
            return null;
        }

        return mediaFile;
    }


    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
            }
        }
        return cameraId;
    }

    public Camera getCameraInstance(){
        cameraId = findFrontFacingCamera();
        int idd = 0;
        if (cameraId < 0) {
            idd = 4;
        } else {
            idd = cameraId;
        }
        Camera c = null;
        try {
            c = Camera.open(idd); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}
