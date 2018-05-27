package com.site.app;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
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
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;
import android.provider.MediaStore.Files.FileColumns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.site.app.MainActivity.MY_PERMISSIONS_CAM;
import static com.site.app.MainActivity.MY_PERMISSIONS_STO;

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

    public static final int MY_PERMISSIONS_CAM = 0;
    public static final int MY_PERMISSIONS_STO = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);


        requestStorage();


        if (checkCameraHardware(this)) {
            try {
                cameraId = findFrontFacingCamera();
                if (cameraId < 0) {
                } else {
                    mCamera = Camera.open(cameraId);
                }
                try {
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.setPictureFormat(ImageFormat.JPEG);
                    List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
                    Camera.Size size = sizes.get(0);
                    parameters.setPictureSize(size.width, size.height);
                    if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }
                    List<String> supportedFlashModes = parameters.getSupportedFlashModes();
                    if (supportedFlashModes != null && supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                        parameters.setFocusMode(Camera.Parameters.FLASH_MODE_ON);
                    }
                    //STEP #1: Get rotation degrees
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
                    int rotation = FullscreenActivity.this.getWindowManager().getDefaultDisplay().getRotation();
                    int degrees = 0;
                    switch (rotation) {
                        case Surface.ROTATION_0: degrees = 0; break; //Natural orientation
                        case Surface.ROTATION_90: degrees = 90; break; //Landscape left
                        case Surface.ROTATION_180: degrees = 180; break;//Upside down
                        case Surface.ROTATION_270: degrees = 270; break;//Landscape right
                    }
                    int rotate = (info.orientation - degrees + 360) % 360;

                    //STEP #2: Set the 'rotation' parameter
                    Camera.Parameters params = mCamera.getParameters();
                    params.setRotation(rotate);
                    mCamera.setParameters(parameters);
                }
                catch (Exception e) {

                }
                // Create our Preview view and set it as the content of our activity.
                mPreview = new CameraPreview(FullscreenActivity.this, mCamera);
                FrameLayout preview = (FrameLayout) findViewById(R.id.cam);
                preview.addView(mPreview);
                mCamera.startPreview();
            }
            catch (Exception e) {
                Toast.makeText(FullscreenActivity.this, "open camera failed." + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        ImageButton next = (ImageButton) findViewById(R.id.btn_next);
        ImageButton capture = (ImageButton) findViewById(R.id.btn_capture);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.unlock();
                    mCamera.release();
                    mCamera = null;
                }
                Intent i = new Intent(FullscreenActivity.this, FormEditting.class);
                startActivity(i);
                finish();
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

    @Override
    public void onBackPressed() {
        // Write your code here
        finish();
        super.onBackPressed();
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
                Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length);

                ExifInterface exif=new ExifInterface(pictureFile.toString());

                Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
                if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
                    realImage= rotate(realImage, 90);
                } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
                    realImage= rotate(realImage, 270);
                } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
                    realImage= rotate(realImage, 180);
                } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")){
                    realImage= rotate(realImage, 90);
                }

                boolean bo = realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.write(data);
                fos.close();
                mCamera.startPreview();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

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


    public  void requestCam() {
        if (ContextCompat.checkSelfPermission(FullscreenActivity.this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(FullscreenActivity.this,
                    android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(FullscreenActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_CAM);

            } else {
                ActivityCompat.requestPermissions(FullscreenActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_CAM);
            }
        } else {
            // Permission has already been granted
        }
    }

    public  void requestStorage() {
        if (ContextCompat.checkSelfPermission(FullscreenActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(FullscreenActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(FullscreenActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_STO);

            } else {
                ActivityCompat.requestPermissions(FullscreenActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_STO);
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_CAM: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(FullscreenActivity.this, "open camera failed.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        requestCam();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                requestStorage();
            }
        }, 300);

    }

}
