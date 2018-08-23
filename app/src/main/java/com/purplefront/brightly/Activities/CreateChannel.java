package com.purplefront.brightly.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
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
import com.purplefront.brightly.API.RestApiMethods;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddChannelResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.ImageChooser_Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class CreateChannel extends BaseActivity {
    View view;
   // ImageView imageView_channelImage;
    EditText create_channelName;
    EditText create_channelDescription;
    SimpleDraweeView imageView_channelImage;
    Button btn_createChannel;
    int PICK_IMAGE_REQ = 77;

    String userId;
    String channel_name = "";
    String channel_description = "";
    ResizeOptions img_resize_opts;
    String encoded_string = "";
    ImageChooser_Crop imgImageChooser_crop;
    String image_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Create Channel");

        userId = getIntent().getStringExtra("userId");
        imageView_channelImage = (SimpleDraweeView) findViewById(R.id.imageView_channelImage);
        create_channelName = (EditText) findViewById(R.id.create_channelName);
        create_channelDescription = (EditText) findViewById(R.id.create_channelDescription);
        btn_createChannel = (Button) findViewById(R.id.btn_createChannel);

        Glide.with(CreateChannel.this)
                .load(R.drawable.no_image_available)
                .centerCrop()
                /*.transform(new CircleTransform(HomeActivity.this))
                .override(50, 50)*/
                .into(imageView_channelImage);

        btn_createChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        imageView_channelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    imgImageChooser_crop = new ImageChooser_Crop(CreateChannel.this);
                    Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                    if (intent == null) {
                        //PermissionUtil.
                    } else {
                        startActivityForResult(intent, PICK_IMAGE_REQ);
                    }

            }
        });
    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        channel_name = create_channelName.getText().toString();
        channel_description = create_channelDescription.getText().toString();

        // Check if all strings are null or not
        if (channel_name.equals("") || channel_name.length() == 0
                || channel_description.equals("") || channel_description.length() == 0) {

            new CustomToast().Show_Toast(CreateChannel.this, create_channelDescription,
                    "Both fields are required.");
        }

            // Else do signup or do your stuff
        else {
            getAddChannels();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                               Bitmap bitmap = MediaStore.Images.Media.getBitmap(CreateChannel.this.getContentResolver(), picUri);
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setMinCropResultSize(500, 500)
                                .setMaxCropResultSize(bitmap.getWidth(), bitmap.getHeight())
//                                .setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .start(CreateChannel.this);
                    }
                } catch (Exception e) {
                    Toast.makeText(CreateChannel.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {


                    Uri resultUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(CreateChannel.this.getContentResolver(), resultUri);

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
                    imageView_channelImage.setImageRequest(imageRequest2);
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


    public void getAddChannels() {
        try {

            if (CheckNetworkConnection.isOnline(CreateChannel.this)) {
                showProgress();
                Call<AddChannelResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(CreateChannel.this).getAddChannels(userId, channel_name, channel_description, encoded_string, image_name);
                callRegisterUser.enqueue(new ApiCallback<AddChannelResponse>(CreateChannel.this) {
                    @Override
                    public void onApiResponse(Response<AddChannelResponse> response, boolean isSuccess, String message) {
                        AddChannelResponse addChannelResponse = response.body();
                        if (isSuccess) {

                            if (addChannelResponse != null) {

                                setAddChannelCredentials(addChannelResponse);
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



    private void setAddChannelCredentials(AddChannelResponse addChannelResponse) {


        String message = addChannelResponse.getMessage();

        if(message.equals("success"))
        {
            finishIntent(CreateChannel.this, MyChannel.class);
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        }
        else {
            showLongToast(CreateChannel.this, message);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
