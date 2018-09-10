package com.purplefront.brightly.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.DeleteChannelResponse;
import com.purplefront.brightly.Modules.UpdateChannelResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.ImageChooser_Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class EditChannelInfo extends BaseActivity {

    SimpleDraweeView imageView_editChannelImage;
    EditText edit_channelName;
    EditText edit_channelDescription;
    Button btn_editChannel;

    String userId;
    String channel_name = "";
    String channel_description = "";
    String encoded_string = "";
    String image_name = "";
    String channel_id = "";

    int PICK_IMAGE_REQ = 77;
    ResizeOptions img_resize_opts;
    ChannelListModel chl_modl_obj;
    ImageChooser_Crop imgImageChooser_crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_channel_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        userId = getIntent().getStringExtra("userId");
        /*channel_id = getIntent().getStringExtra("channel_id");
        channel_name = getIntent().getStringExtra("channel_name");
        channel_description = getIntent().getStringExtra("channel_description");
        encoded_string = getIntent().getStringExtra("encoded_string");
        image_name = getIntent().getStringExtra("image_name");*/

        chl_modl_obj = getIntent().getParcelableExtra("model_obj");
        channel_id = chl_modl_obj.getChannel_id();
        channel_name = chl_modl_obj.getChannel_name();
        channel_description = chl_modl_obj.getDescription();
        encoded_string = chl_modl_obj.getCover_image();
        image_name = chl_modl_obj.getImage_name();


        imageView_editChannelImage = (SimpleDraweeView) findViewById(R.id.imageView_editChannelImage);
        edit_channelName = (EditText) findViewById(R.id.edit_channelName);
        edit_channelDescription = (EditText) findViewById(R.id.edit_channelDescription);
        btn_editChannel = (Button) findViewById(R.id.btn_editChannel);

        edit_channelName.setText(channel_name);
        edit_channelDescription.setText(channel_description);

        if(!userId.equalsIgnoreCase(chl_modl_obj.getCreated_by())) {
            edit_channelName.setEnabled(false);
            edit_channelDescription.setEnabled(false);
            btn_editChannel.setVisibility(View.GONE);
            setTitle("Channel Info");
        }
        else {
            btn_editChannel.setVisibility(View.VISIBLE);
            setTitle("Edit Channel Info");
        }
        btn_editChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        imageView_editChannelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId.equalsIgnoreCase(chl_modl_obj.getCreated_by())) {
                    imgImageChooser_crop = new ImageChooser_Crop(EditChannelInfo.this);
                    Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                    if (intent == null) {
                        //PermissionUtil.
                    } else {
                        startActivityForResult(intent, PICK_IMAGE_REQ);
                    }
                }
            }
        });

        if (!encoded_string.isEmpty()) {

            Glide.with(EditChannelInfo.this)
                    .load(encoded_string)
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(imageView_editChannelImage);
        } else {
            Glide.with(EditChannelInfo.this)
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(imageView_editChannelImage);

            /*Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.no_image_available))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .build();
            imageView_editChannelImage.setImageRequest(imageRequest2);*/
        }


    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        channel_name = edit_channelName.getText().toString();
        channel_description = edit_channelDescription.getText().toString();

        // Check if all strings are null or not
        if (channel_name.equals("") || channel_name.length() == 0
                || channel_description.equals("") || channel_description.length() == 0) {

            new CustomToast().Show_Toast(EditChannelInfo.this, edit_channelName,
                    "Both fields are required.");
        }

        // Else do signup or do your stuff
        else {
            getUpdateChannels();
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditChannelInfo.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete....");

        // Setting Dialog Message
        alertDialog.setMessage("You are about to delete the Channel. All the information contained in the Sets & Cards will be lost. ");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.error);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                getDeleteChannel();
                // Write your code here to invoke YES event
//                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
//                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(chl_modl_obj.getCreated_by().equalsIgnoreCase(userId))
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;

            case R.id.delete:

                showAlertDialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDeleteChannel() {
        try {

            if (CheckNetworkConnection.isOnline(EditChannelInfo.this)) {
                showProgress();
                Call<DeleteChannelResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(EditChannelInfo.this).getDeleteChannels(channel_id);
                callRegisterUser.enqueue(new ApiCallback<DeleteChannelResponse>(EditChannelInfo.this) {
                    @Override
                    public void onApiResponse(Response<DeleteChannelResponse> response, boolean isSuccess, String message) {
                        DeleteChannelResponse deleteChannelResponse = response.body();
                        if (isSuccess) {

                            if (deleteChannelResponse != null) {

                                setAddChannelCredentials(deleteChannelResponse);
                                dismissProgress();

                            } else {
                                dismissProgress();

                            }

                        } else {

                            dismissProgress();
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
                    }
                });
            } else {

                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();

            dismissProgress();
        }

    }

    private void setAddChannelCredentials(DeleteChannelResponse deleteChannelResponse) {

        String message = deleteChannelResponse.getMessage();

        if (message.equals("success")) {
            stackClearIntent(EditChannelInfo.this, MyChannel.class);
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);


        } else {
            showLongToast(EditChannelInfo.this, message);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == OPEN_SETTINGS) {
            Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
            if (intent == null) {
                //PermissionUtil.
            } else {
                startActivityForResult(intent, PICK_IMAGE_REQ);
            }

        }*/
        if (requestCode == PICK_IMAGE_REQ) {
            if (resultCode == RESULT_OK) {

                try {

                    Uri picUri = imgImageChooser_crop.getPickImageResultUri(data);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditChannelInfo.this.getContentResolver(), picUri);
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setMinCropResultSize(500, 500)
                                .setMaxCropResultSize(bitmap.getWidth(), bitmap.getHeight())
//                                .setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .start(EditChannelInfo.this);
                    }
                } catch (Exception e) {
                    Toast.makeText(EditChannelInfo.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {


                    Uri resultUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditChannelInfo.this.getContentResolver(), resultUri);

                    encoded_string = getStringImage(bitmap);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    image_name = sdf.format(new Date());
                   /* if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgPet.setImageDrawable(drawable);
                    }*/
                    final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)
                                    .setResizeOptions(img_resize_opts)
                                    .build();
                    imageView_editChannelImage.setImageRequest(imageRequest2);
                    //   imgPet.getHierarchy().setRoundingParams(roundingParams);
                    // Call_pet_photo_update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
        /*try {
        InputStream inputStream = null;//You can get an inputStream using any IO API

            inputStream = new FileInputStream(fileName);

        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encodedString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }*/

    }

    public void getUpdateChannels() {
        try {

            if (CheckNetworkConnection.isOnline(EditChannelInfo.this)) {
                showProgress();
                Call<UpdateChannelResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(EditChannelInfo.this).getUpdateChannels(userId, channel_name, channel_description, encoded_string, image_name, channel_id);
                callRegisterUser.enqueue(new ApiCallback<UpdateChannelResponse>(EditChannelInfo.this) {
                    @Override
                    public void onApiResponse(Response<UpdateChannelResponse> response, boolean isSuccess, String message) {
                        UpdateChannelResponse updateChannelResponse = response.body();
                        if (isSuccess) {

                            if (updateChannelResponse != null) {

                                setAddChannelCredentials(updateChannelResponse);
                                dismissProgress();

                            } else {
                                dismissProgress();

                            }

                        } else {

                            dismissProgress();
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        dismissProgress();
                    }
                });
            } else {

                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();

            dismissProgress();
        }

    }

    private void setAddChannelCredentials(UpdateChannelResponse updateChannelResponse) {

        String message = updateChannelResponse.getMessage();

        if (message.equals("success")) {
            stackClearIntent(EditChannelInfo.this, MyChannel.class);
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);


        } else {
            showLongToast(EditChannelInfo.this, message);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
