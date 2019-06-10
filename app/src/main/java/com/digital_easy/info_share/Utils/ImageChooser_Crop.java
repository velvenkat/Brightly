package com.digital_easy.info_share.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;

import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niranjan Reddy on 28-02-2018.
 */

public class ImageChooser_Crop {

    Activity call_activity;
    final int PIC_CROP = 11;

    String File_Name;

    public ImageChooser_Crop(Activity activity, String name) {
        call_activity = activity;
        File_Name = name + ".png";
    }

    public Intent getPickImageChooserIntent() {

        if (PermissionUtil.hasPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, call_activity, BrightlyNavigationActivity.PERMISSION_REQ_CODE_IMAGE)) {
            Uri outputFileUri = getCaptureImageOutputUri();

            List<Intent> allIntents = new ArrayList<>();
            PackageManager packageManager = call_activity.getPackageManager();

            // collect all camera intents
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
            for (ResolveInfo res : listCam) {
                Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }
                allIntents.add(intent);
            }

            // collect all gallery intents
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
            for (ResolveInfo res : listGallery) {
                Intent intent = new Intent(galleryIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setPackage(res.activityInfo.packageName);
                allIntents.add(intent);
            }

            // the main intent is the last in the list  so pickup the useless one
            Intent mainIntent = allIntents.get(allIntents.size() - 1);
            for (Intent intent : allIntents) {
                if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                    mainIntent = intent;
                    break;
                }
            }
            allIntents.remove(mainIntent);

            // Create a chooser from the main intent
            Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");


            // Add all other intents
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

            return chooserIntent;
        } else
            return null;
    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = call_activity.getExternalCacheDir();
        if (getImage != null) {
            //  File file = new File(getImage.getPath(), File_Name);

            outputFileUri = Uri.fromFile(new File(getImage.getPath(), File_Name));
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        if (!isCamera) {
            if (data.getData() != null) {
                return data.getData();
            } else {
                return getCaptureImageOutputUri();
            }
        } else {
            return getCaptureImageOutputUri();
        }

        //return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    public Intent performCrop(Uri picUri, boolean isShare, int imgW, int imgH) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cropIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }


        cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri

        cropIntent.setDataAndType(picUri, "image/*");
        // set crop properties here
        cropIntent.putExtra("crop", true);
        // indicate aspect of desired crop
       /* cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);*/
       /* cropIntent.putExtra("max-width", imgW);
        cropIntent.putExtra("max-height", imgH);*/
        cropIntent.putExtra("min-width", imgW);
        cropIntent.putExtra("min-height", imgH);

        // indicate output X and Y
      /*  if(isShare){
            cropIntent.putExtra("outputX", 150);
            //   cropIntent.putExtra("scale", true);
            cropIntent.putExtra("outputY", 150);
        }
        else {
            cropIntent.putExtra("outputX", 128);
            //   cropIntent.putExtra("scale", true);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
        }*/
       /* cropIntent.putExtra("outputX", 150);
        //   cropIntent.putExtra("scale", true);
        cropIntent.putExtra("outputY", 150);*/
        cropIntent.putExtra("return-data", true);
        cropIntent.putExtra("scaleUpIfNeeded", true);
        // cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);

        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // start the activity - we handle returning     in onActivityResult

        return cropIntent;


    }

    public Bitmap getBitmap(Intent data) {

        // get the returned data
        Bitmap selectedBitmap = null;

        Bundle extras = data.getExtras();


        try {
            selectedBitmap = extras.getParcelable("data");
            if (selectedBitmap == null) {
                return null;
            }


        } catch (Exception e) {


            try {
                // This solution is work for A1 plus mobile. We are write the below code for A1 mobile previously bitmap is getting null.
                Uri uri = data.getData();
                selectedBitmap = MediaStore.Images.Media.getBitmap(call_activity.getContentResolver(), uri);
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
            e.printStackTrace();

        }

        return selectedBitmap;

    }


}
