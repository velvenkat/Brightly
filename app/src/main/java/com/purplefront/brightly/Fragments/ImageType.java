package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageType extends BaseFragment {

    View frag_rootView;
    EditText create_cardName;
    EditText create_cardDescription;
    ImageView image_cardImage;
    ResizeOptions img_resize_opts;
    ImageChooser_Crop imgImageChooser_crop;
    Button btn_createCard;
    int PICK_IMAGE_REQ = 77;
    ResizeOptions mResizeOptions;
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
    String Created_by;
    boolean isCreateCard;

    public ImageType() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserInterface) {
            userModule = ((UserInterface) context).getUserMode();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag_rootView = inflater.inflate(R.layout.fragment_image_type, container, false);
        context = frag_rootView.getContext();

        userId = userModule.getUserId();
        set_id = userModule.getSet_id();
        set_name = userModule.getSet_name();


        image_cardImage = (ImageView) frag_rootView.findViewById(R.id.image_cardImage);
        create_cardName = (EditText) frag_rootView.findViewById(R.id.create_cardName);
        create_cardDescription = (EditText) frag_rootView.findViewById(R.id.create_cardDescription);
        btn_createCard = (Button) frag_rootView.findViewById(R.id.btn_createCard);

        Bundle bundle = getArguments();
        isCreateCard = bundle.getBoolean("isCreate");
        Created_by=bundle.getString("created_by");
        boolean load_def_img = true;

        frag_rootView.addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(
                            View view,
                            int left,
                            int top,
                            int right,
                            int bottom,
                            int oldLeft,
                            int oldTop,
                            int oldRight,
                            int oldBottom) {
                        final int imageSize = ((right - 10) - (left - 10));
                        mResizeOptions = new ResizeOptions(imageSize, imageSize);
                    }
                });

        if (!isCreateCard) {
            btn_createCard.setText("UPDATE CARD");
            create_cardName.setText(userModule.getCard_name());
            create_cardDescription.setText(userModule.getCard_description());
            if(!Created_by.equalsIgnoreCase(userId)){
                image_cardImage.setClickable(false);
                create_cardName.setEnabled(false);
                create_cardDescription.setEnabled(false);
                btn_createCard.setVisibility(View.GONE);
            }
            if (userModule.getType().equalsIgnoreCase("image")) {
                load_def_img = false;

                Glide.with(getActivity())
                        .load(userModule.getCard_multimedia_url())
                        .centerCrop()
                        /*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*/
                        .into(image_cardImage);
               /* RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //  layoutParams.setMargins(0,0,0,0);
                image_cardImage.setLayoutParams(layoutParams);*/

             //   image_cardImage.setScaleType(ImageView.ScaleType.FIT_XY);


                /*final ImageRequest imageRequest =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(userModule.getCard_multimedia_url()))
                                .setResizeOptions(mResizeOptions)
                                .build();
                image_cardImage.setImageRequest(imageRequest);*/

            } else {
                load_def_img = true;

            }
        }
        else {
            btn_createCard.setText("CREATE CARD");
        }
        if (load_def_img) {
           /* Glide.with(getActivity())
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    *//*.transform(new CircleTransform(HomeAct/ivity.this))
                    .override(50, 50)*//*
                    .into(image_cardImage);*/
            image_cardImage.setImageResource(R.drawable.no_image_available);


            /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            //  layoutParams.setMargins(0,0,0,0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);*/
          //  image_cardImage.setLayoutParams(layoutParams);


           // image_cardImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        btn_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        image_cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCreateCard || encoded_string.equalsIgnoreCase("image_to_text")) {


                    /*imgImageChooser_crop = new ImageChooser_Crop(getActivity());
                    Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                    if (intent == null) {
                        //PermissionUtil.
                    } else {
                        startActivityForResult(intent, PICK_IMAGE_REQ);
                    }*/
                    imageChooserIntent();
                } else {
                    if (!userModule.getType().equalsIgnoreCase("image") && encoded_string.equalsIgnoreCase("")) {
                        imageChooserIntent();
                    } else
                        setBottomDialog();
                }
            }
        });

        frag_rootView.setFocusableInTouchMode(true);
        frag_rootView.requestFocus();
        frag_rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {

                 getActivity().setResult(Activity.RESULT_CANCELED);
                 getActivity().finish();
                }
                return true;
            }
        });
        return frag_rootView;
    }


    public void imageChooserIntent() {
        imgImageChooser_crop = new ImageChooser_Crop(getActivity());
        Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
        if (intent == null) {
            //PermissionUtil.
        } else {
            startActivityForResult(intent, PICK_IMAGE_REQ);
        }
    }

    public void setBottomDialog() {

        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.dialog_view_layout); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        ListView list_SettingsMenu = (ListView) mBottomSheetDialog.getWindow().findViewById(R.id.list_view_dialog);
        ArrayList<String> menu_list = new ArrayList<>();
        menu_list.add("Choose new image");
        menu_list.add("Remove image");

        ArrayAdapter<String> menu_itmes = new ArrayAdapter<String>(getContext(), R.layout.menu_row_diualog, R.id.dialog_menu_textView,
                menu_list);
        list_SettingsMenu.setAdapter(menu_itmes);
        list_SettingsMenu.requestFocus();
        Button btnCancel = (Button) mBottomSheetDialog.getWindow().findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        list_SettingsMenu.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getContext(),"Position :"+position,Toast.LENGTH_SHORT).show();
                        mBottomSheetDialog.dismiss();
                        //audio_uri = null;
                        //tempMp3File = null;
                        //release_media();
                        //text_audioFile.setVisibility(View.VISIBLE);
                        // rl_audio_player.setVisibility(View.GONE);
                        switch (position) {
                            case 1:
                                if (userModule.getType().equalsIgnoreCase("image"))
                                    encoded_string = "image_to_text";
                                else
                                    encoded_string = "";
                                Glide.with(getActivity())
                                        .load(R.drawable.no_image_available)
                                        .centerCrop()
                                        /*.transform(new CircleTransform(HomeActivity.this))
                                        .override(50, 50)*/
                                        .into(image_cardImage);
                                break;
                            case 0:
                                imageChooserIntent();
                                break;

                        }
                    }
                }
        );

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
        } else if (encoded_string.equals("") || encoded_string.length() == 0 || encoded_string.equalsIgnoreCase("image_to_text")) {
           /* new CustomToast().Show_Toast(getActivity(), image_cardImage,
                    "Image is required.");*/
            if(userModule.getType().equals("image")){
                if(encoded_string.equalsIgnoreCase("image_to_text"))
                    type = "text";
                else {
                    encoded_string = "old";
                    type="image";
                }
            }
            else
            type = "text";

            getAddCards();
        }

        // Else do signup or do your stuff
        else {
            type = "image";
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    if (picUri != null) {
                       /* Intent intent = imgImageChooser_crop.performCrop(picUri, false, 150, 150);
                        startActivityForResult(intent, PIC_CROP);*/
                        // for fragment (DO NOT use `getActivity()`)
                        CropImage.activity(picUri)
                                .setMinCropResultSize(250, 250)
                                .setMaxCropResultSize(bitmap.getWidth(), bitmap.getHeight())
//                                .setAspectRatio(1, 1)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .start(context, this);
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
                   /* if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgPet.setImageDrawable(drawable);
                    }*/
                    /*final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)
                                    .setResizeOptions(mResizeOptions)
                                    .build();
                    image_cardImage.setImageRequest(imageRequest2);*/

                 /*   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //     layoutParams.setMargins(0,0,0,0);
                    image_cardImage.setLayoutParams(layoutParams);*/
                 //   image_cardImage.setScaleType(ImageView.ScaleType.FIT_XY);
                   /* final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)
                                    .setResizeOptions(mResizeOptions)
                                    .build();
                    image_cardImage.setImageRequest(imageRequest2);*/
                    Glide.with(getActivity())
                            .load(resultUri)
                            .fitCenter()
                            /*.transform(new CircleTransform(HomeActivity.this))
                            .override(50, 50)*/
                            .into(image_cardImage);

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
                Call<AddMessageResponse> callRegisterUser;
                if (isCreateCard) {
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getAddCardsList(type, userId, set_id, card_name, card_description, encoded_string, image_name);
                } else
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getUpdateCardsList(type, userId, set_id, userModule.getCard_id(), card_name, card_description, encoded_string, image_name);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                dismissProgress();
                                setAddSetCredentials(addMessageResponse);


                            } else {
                                dismissProgress();

                            }

                        } else {

                            dismissProgress();
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        if (message.equals("timeout")) {
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

        if (message.equals("success")) {
           /* Intent intent = new Intent(getActivity(), MySetCards.class);
            intent.putExtra("set_id", set_id);
            intent.putExtra("set_name", set_name);
            intent.putExtra("userId", userId);*/
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
           /* getActivity().finish();
            startActivity(intent);*/
            getActivity().overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        } else {
            showLongToast(getActivity(), message);
        }
    }


}
