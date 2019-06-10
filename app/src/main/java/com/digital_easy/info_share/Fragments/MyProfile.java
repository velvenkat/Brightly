package com.digital_easy.info_share.Fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.GeneralVarModel;
import com.digital_easy.info_share.Modules.MyProfileResponse;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;

import com.digital_easy.info_share.Utils.Util;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends BaseFragment implements View.OnClickListener {

    View rootView;
    private static FragmentManager fragmentManager;
    private TextView input_email, input_company;
    private SimpleDraweeView Image_profile;
    private TextView User_Name, User_Phone;
    FloatingActionButton edit_profile;

    RealmModel user_obj;


    Realm realm;
    RealmResults<RealmModel> realmModel;

    String user_ID;
    String userName;
    String userCompanyName;
    String userEmail;
    String phoneNumber;
    String imageProfile;
    String image_name;

    public MyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        fragmentManager = getActivity().getSupportFragmentManager();
        // Set title bar
        ((BrightlyNavigationActivity) getActivity()).setActionBarTitle("My Profile");
        setHasOptionsMenu(true);
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
                    //return true;
                }

                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    // Initiate Views
    private void initViews() {


        edit_profile = (FloatingActionButton) rootView.findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(this);
        input_email = (TextView) rootView.findViewById(R.id.input_email);
        input_company = (TextView) rootView.findViewById(R.id.input_company);
        Image_profile = (SimpleDraweeView) rootView.findViewById(R.id.Image_profile);
        User_Name = (TextView) rootView.findViewById(R.id.User_Name);
        User_Phone = (TextView) rootView.findViewById(R.id.User_Phone);


        realmModel.load();
        for (RealmModel model : realmModel) {

            user_ID = model.getUser_Id();
            User_Name.setText(model.getUser_Name());
            User_Phone.setText(model.getUser_PhoneNumber());
            //Toast.makeText(getContext(), "Prof_init" + model.getUser_CompanyName(), Toast.LENGTH_LONG).show();
            input_company.setText(model.getUser_CompanyName());
            // input_company.postInvalidate();

            input_email.setText(model.getUser_Email());

            //input_email.postInvalidate();
            //imageProfile = model.getImage();


            ((BrightlyNavigationActivity) getActivity()).headerText_Name.setText(model.getUser_Name());
            ((BrightlyNavigationActivity) getActivity()).headerText_Phone.setText(model.getUser_PhoneNumber());
            String prof_img_url = null;
            if (model.getImage() != null && !model.getImage().trim().equals("")) {
                prof_img_url = model.getImage();
            } else {
                GeneralVarModel prof_model = ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getProf_default_img();
                if (prof_model != null) {
                    prof_img_url = prof_model.getFetch_key();
                }
            }
            ResizeOptions mResizeOptions = new ResizeOptions(150, 150);
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
                                .setResizeOptions(mResizeOptions)
                                .build();
                Image_profile.setImageRequest(imageRequest2);
            }

/*

            if (model.getImage() != null && !model.getImage().trim().equals("")) {

                Glide.with(getActivity())
                        .load(imageProfile)
                        .centerCrop()
                        .transform(new CircleTransform(getActivity()))
//                        .override(50, 50)
                        .into(Image_profile);

            } else {
                Glide.with(getActivity())
                        .load(R.drawable.default_user_image)
                        .centerCrop()
                        .transform(new CircleTransform(getActivity()))
                        */
            /*.override(50, 50)*//*

                        .into(Image_profile);
            }
*/

        }

    }

    public void getMyProfile() {
        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                Call<MyProfileResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getMyProfile(user_ID);
                callRegisterUser.enqueue(new ApiCallback<MyProfileResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<MyProfileResponse> response, boolean isSuccess, String message) {
                        MyProfileResponse profileResponse = response.body();

                        if (isSuccess) {

                            if (profileResponse != null) {

                                setProfileCredentials(profileResponse);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            showLongToast(getActivity(), "Something went Wrong, Please try Later");


        }
    }

    /**
     * @param profileResponse
     */
    private void setProfileCredentials(MyProfileResponse profileResponse) {

        phoneNumber = profileResponse.getMobile();
        userName = profileResponse.getName();
        userCompanyName = profileResponse.getCompany_name();
        imageProfile = profileResponse.getImage();
        userEmail = profileResponse.getEmail();
        image_name = profileResponse.getImage_name();

        User_Name.setText(userName);
        User_Phone.setText(phoneNumber);
        input_email.setText(userEmail);
        input_company.setText(userCompanyName);
        ResizeOptions mResizeOptions = new ResizeOptions(150, 150);

        if (profileResponse.getImage() != null) {



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
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(profileResponse.getImage()))
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
                            .setResizeOptions(mResizeOptions)
                            .build();
            Image_profile.setImageRequest(imageRequest2);
        }


        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        // delete all realm objects
        realm.deleteAll();

        //commit realm changes
        realm.commitTransaction();
        if (user_ID != null) {

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


        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.edit_profile:

                // Replace edit frgament with animation
                    /*fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                            .replace(R.id.frag_container, new EditProfile(),
                                    Util.Edit_Profile).commit();*/
                Fragment fragment = new EditProfile();
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Edit_Profile, fragment, true);
                break;
        }

    }

}
