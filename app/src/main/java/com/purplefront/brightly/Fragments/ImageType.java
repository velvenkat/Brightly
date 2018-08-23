package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.CreateCards;
import com.purplefront.brightly.Activities.MySetCards;
import com.purplefront.brightly.Application.UserInterface;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.UserModule;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.ImageChooser_Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageType extends BaseFragment {

    View view;
    EditText create_cardName;
    EditText create_cardDescription;
    SimpleDraweeView image_cardImage;
    ResizeOptions img_resize_opts;
    ImageChooser_Crop imgImageChooser_crop;
    Button btn_createCard;
    int PICK_IMAGE_REQ = 77;

    Context context;
    UserModule userModule;

    String userId;
    String set_id;
    String set_name;
    String card_name = "";
    String card_description = "";
    String encoded_string = "";
    String image_name = "";
    String type = "";

    public ImageType() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof UserInterface)
        {
          userModule=((UserInterface)context).getUserMode();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_image_type, container, false);
        context =view.getContext();
        /*realm = Realm.getDefaultInstance();
        initViews();
        setListeners();*/
        context = view.getContext();

        userId = userModule.getUserId();
        set_id = userModule.getSet_id();
        set_name = userModule.getSet_name();


        image_cardImage = (SimpleDraweeView) view.findViewById(R.id.image_cardImage);
        create_cardName = (EditText) view.findViewById(R.id.create_cardName);
        create_cardDescription = (EditText) view.findViewById(R.id.create_cardDescription);
        btn_createCard = (Button)view.findViewById(R.id.btn_createCard);

        Glide.with(getActivity())
                .load(R.drawable.no_image_available)
                .centerCrop()
                /*.transform(new CircleTransform(HomeActivity.this))
                .override(50, 50)*/
                .into(image_cardImage);

        btn_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        image_cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgImageChooser_crop = new ImageChooser_Crop(getActivity());
                Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                if (intent == null) {
                    //PermissionUtil.
                } else {
                    startActivityForResult(intent, PICK_IMAGE_REQ);
                }

            }
        });



        return view;
    }


    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        card_name = create_cardName.getText().toString();
        card_description = create_cardDescription.getText().toString();

        // Check if all strings are null or not
        if (card_name.equals("") || card_name.length() == 0
                || card_description.equals("") || card_description.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), create_cardName,
                    "Both fields are required.");
        }
        else if(encoded_string.equals("") || encoded_string.length() == 0)
        {
            new CustomToast().Show_Toast(getActivity(), image_cardImage,
                    "Image is required.");
        }

        // Else do signup or do your stuff
        else {
            getAddCards();
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
                               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setMinCropResultSize(500, 500)
                                .setMaxCropResultSize(bitmap.getWidth(), bitmap.getHeight())
//                                .setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .start(getContext(), this);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {


                    Uri resultUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);

                    encoded_string = getStringImage(bitmap);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    image_name = sdf.format(new Date());
                    type = "image";
                   /* if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgPet.setImageDrawable(drawable);
                    }*/
                    final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)
                                    .setResizeOptions(img_resize_opts)
                                    .build();
                    image_cardImage.setImageRequest(imageRequest2);
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

    private void getAddCards() {

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getAddCardsList(type, userId, set_id, card_name, card_description, encoded_string, image_name );
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {

                                setAddSetCredentials(addMessageResponse);
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

                        if(message.equals("timeout"))
                        {
                            showLongToast(getActivity(), "Internet is slow, please try again.");
                        }
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


    private void setAddSetCredentials(AddMessageResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();

        if(message.equals("success"))
        {
            Intent intent = new Intent(getActivity(), MySetCards.class);
            intent.putExtra("set_id", set_id);
            intent.putExtra("set_name", set_name);
            intent.putExtra("userId", userId);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        }

        else {
            showLongToast(getActivity(), message);
        }
    }



}
