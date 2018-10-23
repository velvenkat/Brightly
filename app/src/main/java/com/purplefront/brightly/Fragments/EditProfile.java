package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.EditProfileResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.CircleTransform;
import com.purplefront.brightly.Utils.ImageChooser_Crop;
import com.purplefront.brightly.Utils.Util;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends BaseFragment{

    View rootView;
    Context context;
    FragmentManager fragmentManager;
    private EditText input_email, input_company, input_name, input_phone;

    SimpleDraweeView Image_profile;
    ResizeOptions img_resize_opts;
    ImageChooser_Crop imgImageChooser_crop;
    int PICK_IMAGE_REQ = 77;

    Realm realm;
    RealmResults<RealmModel> realmModel;

    String user_ID;
    String userName = "";
    String userCompanyName = "";
    String userEmail = "";
    String phoneNumber = "";
    String imageProfile = "";
    String image_old = "";
    String image_name = "";
    RealmModel user_obj;
    public EditProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        user_obj=((BrightlyNavigationActivity)getActivity()).getUserModel();
        // Set title bar
        setHasOptionsMenu(true);
        context = getContext();
        ((BrightlyNavigationActivity) getActivity()).setActionBarTitle("Edit Profile");

        //((BrightlyNavigationActivity) getActivity()).toggle.setDrawerIndicatorEnabled(true);

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();

        initViews();
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    //         getActivity().finish();
                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                    return true;
                }
                return false;
            }
        });
        return  rootView;
    }


    // Initiate Views
    private void initViews() {


        input_email = (EditText) rootView.findViewById(R.id.input_email);
        input_company = (EditText) rootView.findViewById(R.id.input_company);
        Image_profile = (SimpleDraweeView) rootView.findViewById(R.id.Image_profile);
        input_name = (EditText) rootView.findViewById(R.id.input_name);
        input_phone = (EditText) rootView.findViewById(R.id.input_phone);


        realmModel.load();
        for(RealmModel model:realmModel){
            user_ID = model.getUser_Id();
            input_name.setText( model.getUser_Name());
            input_phone.setText(model.getUser_PhoneNumber());
            input_email.setText(model.getUser_Email());
            input_company.setText(model.getUser_CompanyName());
            imageProfile = model.getImage();
            image_name = model.getImage_name();

            if (!model.getImage().isEmpty()) {

                Glide.with(getActivity())
                        .load(model.getImage())
                        .centerCrop()
                        .transform(new CircleTransform(getActivity()))
//                        .override(50, 50)
                        .into(Image_profile);
            }
            else
            {
                Glide.with(getActivity())
                        .load(R.drawable.default_user_image)
                        .centerCrop()
                        .transform(new CircleTransform(getActivity()))
                        /*.override(50, 50)*/
                        .into(Image_profile);
            }

        }


        if(!imageProfile.isEmpty())
        {
            imageProfile = "old";
        }


        Image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgImageChooser_crop = new ImageChooser_Crop(getActivity());
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
        userName = input_name.getText().toString();
        userCompanyName = input_company.getText().toString();
        userEmail = input_email.getText().toString();
        phoneNumber = input_phone.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Util.regEx);
        Matcher m = p.matcher(userEmail);

        // Check if all strings are null or not
        if (userName.equals("") || userName.length() == 0
                || userCompanyName.equals("") || userCompanyName.length() == 0
                || userEmail.equals("") || userEmail.length() == 0
                || phoneNumber.equals("") || phoneNumber.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), rootView,
                    "All fields are required.");
        }

        // Check if email id valid or not
        else if (!m.find()) {
            new CustomToast().Show_Toast(getActivity(), rootView,
                    "Your Email Id is Invalid.");
        }

        // Else do signup or do your stuff
        else
            getEditProfile();
//           Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.editprofile_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save_edit:

//                getActivity().onBackPressed();

                checkValidation();

//                ((MyChannel) getActivity()).toggle.setDrawerIndicatorEnabled(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void getEditProfile() {
        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<EditProfileResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getEditProfile(user_ID, userName, userEmail, phoneNumber, userCompanyName, imageProfile, image_name );
                callRegisterUser.enqueue(new ApiCallback<EditProfileResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<EditProfileResponse> response, boolean isSuccess, String message) {
                        EditProfileResponse editProfileResponse = response.body();

                        if (isSuccess) {

                            if (editProfileResponse != null) {

                                setEditProfileCredentials(editProfileResponse);
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
                        showLongToast(getActivity(), message);
                    }
                });
            } else {

                showLongToast(getActivity(), "Network Error");
                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showLongToast(getActivity(), "Something went Wrong, Please try Later");

        }
    }

    private void setEditProfileCredentials(EditProfileResponse editProfileResponse) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        // delete all realm objects
        realm.deleteAll();

        //commit realm changes
        realm.commitTransaction();

        phoneNumber = editProfileResponse.getMobile();
        userName = editProfileResponse.getName();
        userCompanyName = editProfileResponse.getCompany_name();
        imageProfile = editProfileResponse.getImage();
        userEmail = editProfileResponse.getEmail();
        image_name = editProfileResponse.getImage_name();

        if (phoneNumber != null) {

            realm.beginTransaction();
            RealmModel realmModel = realm.createObject(RealmModel.class);
            realmModel.setUser_Id(user_ID);
            realmModel.setUser_Name(userName);
            realmModel.setUser_Email(userEmail);
            realmModel.setUser_PhoneNumber(phoneNumber);
            realmModel.setUser_CompanyName(userCompanyName);
            realmModel.setImage(imageProfile);
            realmModel.setImage_name(image_name);
            realm.commitTransaction();

            input_name.setText(realmModel.getUser_Name());
            input_phone.setText(realmModel.getUser_PhoneNumber());
            input_email.setText(realmModel.getUser_Email());
            input_company.setText(realmModel.getUser_CompanyName());

//            ((BrightlyNavigationActivity)getActivity()).setUserModel(realmModel);

            if (!imageProfile.isEmpty()) {

                Glide.with(getActivity())
                        .load(imageProfile)
                        .centerCrop()
                        .transform(new CircleTransform(getActivity()))
//                            .override(60, 60)
                        .into(Image_profile);
            } else {
                Glide.with(getActivity())
                        .load(R.drawable.default_user_image)
                        .centerCrop()
                        .transform(new CircleTransform(getActivity()))
//                            .override(60, 60)
                        .into(Image_profile);
            }

            String message = editProfileResponse.getMessage();

            if (message.equals("success")) {
                ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
            } else {
                showLongToast(getActivity(), message);
            }
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
                                .setMinCropResultSize(200, 200)
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

                    imageProfile = getStringImage(bitmap);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    image_name = sdf.format(new Date());
                   /* if (bitmap != null) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        imgPet.setImageDrawable(drawable);
                    }*/

                    Glide.with(getActivity())
                            .load(resultUri)
                            .centerCrop()
                            .transform(new CircleTransform(getActivity()))
//                        .override(50, 50)
                            .into(Image_profile);
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

}
