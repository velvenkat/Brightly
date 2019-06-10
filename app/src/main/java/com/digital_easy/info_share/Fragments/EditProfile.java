package com.digital_easy.info_share.Fragments;


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

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.digital_easy.info_share.API.RestApiMethods;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.CustomToast;
import com.digital_easy.info_share.Modules.ContactShare;
import com.digital_easy.info_share.Modules.EditProfileResponse;
import com.digital_easy.info_share.Modules.GeneralVarModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;

import com.digital_easy.info_share.Utils.ImageChooser_Crop;
import com.digital_easy.info_share.Utils.Util;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends BaseFragment implements BrightlyNavigationActivity.PermissionResultInterface {

    View rootView;
    Context context;
    FragmentManager fragmentManager;
    private EditText input_email, input_company, input_name, input_phone;

    SimpleDraweeView Image_profile;
    ResizeOptions img_resize_opts;
    ImageChooser_Crop imgImageChooser_crop;
    int PICK_IMAGE_REQ = 77;

    Realm realm;
    RealmResults<RealmModel> realmModel_list;

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
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        // Set title bar
        setHasOptionsMenu(true);
        context = getContext();
        realm = Realm.getDefaultInstance();
        ((BrightlyNavigationActivity) getActivity()).setActionBarTitle("Edit Profile");

        ((BrightlyNavigationActivity) getActivity()).permissionResultInterfaceObj = this;
        //((BrightlyNavigationActivity) getActivity()).toggle.setDrawerIndicatorEnabled(true);


        realmModel_list = realm.where(RealmModel.class).findAllAsync();

        initViews();
        clear_edit_text_focus(input_email);
        clear_edit_text_focus(input_company);
        clear_edit_text_focus(input_name);
        clear_edit_text_focus(input_phone);
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
        return rootView;
    }


    // Initiate Views
    private void initViews() {

        input_email = (EditText) rootView.findViewById(R.id.input_email);
        input_company = (EditText) rootView.findViewById(R.id.input_company);
        Image_profile = (SimpleDraweeView) rootView.findViewById(R.id.Image_profile);
        input_name = (EditText) rootView.findViewById(R.id.input_name);
        input_phone = (EditText) rootView.findViewById(R.id.input_phone);


        realmModel_list.load();
        for (RealmModel model : realmModel_list) {
            user_ID = model.getUser_Id();
            input_name.setText(model.getUser_Name());
            input_phone.setText(model.getUser_PhoneNumber());
            input_email.setText(model.getUser_Email());
            input_company.setText(model.getUser_CompanyName());
            if (model.getImage() != null)
                imageProfile = model.getImage();
            else
                imageProfile = "";
            if (model.getImage_name() != null)
                image_name = model.getImage_name();
            else
                image_name = "";
            String prof_img_url = null;
            if (model.getImage() != null && !model.getImage().trim().equals("")) {
                prof_img_url = model.getImage();
            } else {
                GeneralVarModel prof_model = ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getProf_default_img();
                if (prof_model != null) {
                    prof_img_url = prof_model.getFetch_key();
                }
            }
            if (prof_img_url != null) {
                /*Glide.with(getActivity())
                        .load(imageProfile)
                        .centerCrop()
                        .transform(new CircleTransform(getActivity()))
//                        .override(50, 50)
                        .into(((BrightlyNavigationActivity) getActivity()).headerImage_Profile);
*/
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                //roundingParams.setBorder(color, 1.0f);
                roundingParams.setRoundAsCircle(true);
                ResizeOptions mResizeOptions = new ResizeOptions(150, 150);
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(this.getResources());
                builder.setProgressBarImage(R.drawable.loader);
                builder.setRoundingParams(roundingParams);
                builder.setProgressBarImage(
                        new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
                builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
                GenericDraweeHierarchy hierarchy = builder
                        .setFadeDuration(100)
                        .build();

                Image_profile.setHierarchy(hierarchy);


                final ImageRequest imageRequest =
                        ImageRequestBuilder.newBuilderWithSource(Uri.parse(prof_img_url))
                                .setResizeOptions(mResizeOptions)

                                .build();
                //  Image_profile.getHierarchy().setRoundingParams(roundingParams);
                Image_profile.setImageRequest(imageRequest);
            } else {
                /*Glide.with(getActivity())
                        .load(R.drawable.default_user_image)
                        .centerCrop()
                        .transform(new CircleTransform(getActivity()))
                        *//*.override(50, 50)*//*
                        .into(Image_profile);*/

                Uri uri = new Uri.Builder()
                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                        .path(String.valueOf(R.drawable.default_user_image))
                        .build();
                final ImageRequest imageRequest2 =
                        ImageRequestBuilder.newBuilderWithSource(uri)

                                .build();
                Image_profile.setImageRequest(imageRequest2);
            }

        }


        if (!imageProfile.trim().equals("")) {
            imageProfile = "old";
        }


        Image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if (PermissionUtil.hasPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, getContext(), BrightlyNavigationActivity.PERMISSION_REQ_CODE_IMAGE)) {
                imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "default");
                Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
                if (intent == null) {
                    //PermissionUtil.
                } else {
                    startActivityForResult(intent, PICK_IMAGE_REQ);
                }
                // }

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
        if (userName.trim().equals("")

                || userEmail.trim().equals("")
                || phoneNumber.trim().equals("")) {

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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.editprofile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save_edit:

//                getActivity().onBackPressed();
                item.setEnabled(false);
                checkValidation();

//                ((MyChannel) getActivity()).toggle.setDrawerIndicatorEnabled(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void getEditProfile() {


        if (CheckNetworkConnection.isOnline(getActivity())) {
            showProgress();
              /*  Call<EditProfileResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getEditProfile(user_ID, userName, userEmail, phoneNumber, userCompanyName, imageProfile, image_name );
                callRegisterUser.enqueue(new ApiCallback<EditProfileResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<EditProfileResponse> response, boolean isSuccess, String message) {*/
            RestApiMethods requestInterface = RetrofitInterface.getRestApiMethods(getContext());
            CompositeDisposable mCompositeDisposable = new CompositeDisposable();
            mCompositeDisposable.add(requestInterface.getEditProfile(user_ID, userName, userEmail, phoneNumber, userCompanyName, imageProfile, image_name)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<EditProfileResponse>() {
                        @Override
                        public void onNext(EditProfileResponse genResModel) {


                            dismissProgress();

                            if (genResModel != null) {

                                setEditProfileCredentials(genResModel);


                            }

                        }

                        @Override
                        public void onError(Throwable e) {

                            dismissProgress();
                            Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {

                            dismissProgress();
                        }
                    }));
        }

    }

    private void setEditProfileCredentials(EditProfileResponse editProfileResponse) {

        String message = editProfileResponse.getMessage();

        if (message.equals("success")) {

            realm.beginTransaction();
            // delete all realm objects
            realm.deleteAll();

            //commit realm changes
            realm.commitTransaction();
            //  Realm realm2 = Realm.getDefaultInstance();
            phoneNumber = editProfileResponse.getMobile();
            userName = editProfileResponse.getName();
            userCompanyName = editProfileResponse.getCompany_name();
            imageProfile = editProfileResponse.getImage();
            userEmail = editProfileResponse.getEmail();
            image_name = editProfileResponse.getImage_name();
            ((BrightlyNavigationActivity) getActivity()).set_prof_hdr_image(imageProfile);
            if (phoneNumber != null) {

                realm.beginTransaction();
                RealmModel realmModel = realm.createObject(RealmModel.class);
                realmModel.setUser_Id(user_ID);
                realmModel.setUser_Name(userName);
                realmModel.setUser_Email(userEmail);
                realmModel.setUser_PhoneNumber(phoneNumber);
                //   Toast.makeText(getContext(),"com_res"+userCompanyName,Toast.LENGTH_LONG).show();
                realmModel.setUser_CompanyName(userCompanyName);
                realmModel.setImage(imageProfile);
                realmModel.setImage_name(image_name);
                realm.commitTransaction();

                ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
            } else {
                // showLongToast(getActivity(), message);
                new CustomToast().Show_Toast(getActivity(), rootView,
                        message);
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
                                .setCropMenuCropButtonTitle("Done")
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

                    /*Glide.with(getActivity())
                            .load(resultUri)
                            .centerCrop()
                            .transform(new CircleTransform(getActivity()))
//                        .override(50, 50)
                            .into(Image_profile);*/

                    GenericDraweeHierarchyBuilder builder =
                            new GenericDraweeHierarchyBuilder(getResources());
                    builder.setProgressBarImage(R.drawable.loader);

                    builder.setProgressBarImage(
                            new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
                    builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                    //roundingParams.setBorder(color, 1.0f);
                    roundingParams.setRoundAsCircle(true);
                    GenericDraweeHierarchy hierarchy = builder
                            .setFadeDuration(100)
                            .setRoundingParams(roundingParams)
                            .build();

                    Image_profile.setHierarchy(hierarchy);


                    final ImageRequest imageRequest =
                            ImageRequestBuilder.newBuilderWithSource(resultUri)


                                    .build();
                    Image_profile.setImageRequest(imageRequest);

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

    @Override
    public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list) {
        imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "default");
        Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
        if (intent == null) {
            //PermissionUtil.
        } else {
            startActivityForResult(intent, PICK_IMAGE_REQ);
        }
    }
}
