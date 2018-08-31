package com.purplefront.brightly.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.MyChannel;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.EditProfileResponse;
import com.purplefront.brightly.Modules.MyProfileResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.CircleTransform;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends BaseFragment implements View.OnClickListener{

    View view;
    private static FragmentManager fragmentManager;
    private EditText input_email, input_company;
    private ImageView Image_profile;
    private TextView User_Name, User_Phone;
    FloatingActionButton edit_profile;

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
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        // Set title bar
        ((MyChannel) getActivity()).setActionBarTitle("My Profile");

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        initViews();
        getMyProfile();
        return view;
    }

    // Initiate Views
    private void initViews() {


        edit_profile = (FloatingActionButton) view.findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(this);
        input_email = (EditText) view.findViewById(R.id.input_email);
        input_company = (EditText) view.findViewById(R.id.input_company);
        Image_profile = (ImageView) view.findViewById(R.id.Image_profile);
        User_Name = (TextView) view.findViewById(R.id.User_Name);
        User_Phone = (TextView) view.findViewById(R.id.User_Phone);

        realmModel.load();
        for(RealmModel model:realmModel){
            user_ID = model.getUser_Id();
            User_Name.setText( model.getUser_Name());
            User_Phone.setText(model.getUser_PhoneNumber());
            input_email.setText(model.getUser_Email());
            input_company.setText(model.getUser_CompanyName());

            if( model.getImage() != null) {

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
    private void setProfileCredentials(MyProfileResponse profileResponse ) {

        phoneNumber = profileResponse.getMobile();
        userName = profileResponse.getName();
        userCompanyName  = profileResponse.getCompany_name();
        imageProfile  = profileResponse.getImage();
        userEmail  = profileResponse.getEmail();

        if(phoneNumber!=null) {

            realm.beginTransaction();
            RealmModel realmModel=realm.createObject(RealmModel.class);
            realmModel.setUser_Id(user_ID);
            realmModel.setUser_Name(userName);
            realmModel.setUser_Email(userEmail);
            realmModel.setUser_PhoneNumber(phoneNumber);
            realmModel.setUser_CompanyName(userCompanyName);
            realmModel.setImage(imageProfile);
            realm.commitTransaction();

                User_Name.setText( realmModel.getUser_Name());
                User_Phone.setText(realmModel.getUser_PhoneNumber());
                input_email.setText(realmModel.getUser_Email());
                input_company.setText(realmModel.getUser_CompanyName());

                if(!realmModel.getImage().isEmpty()) {

                    Glide.with(getActivity())
                            .load(realmModel.getImage())
                            .centerCrop()
                            .transform(new CircleTransform(getActivity()))
//                            .override(60, 60)
                            .into(Image_profile);
                }
                else
                {
                    Glide.with(getActivity())
                            .load(R.drawable.default_user_image)
                            .centerCrop()
                            .transform(new CircleTransform(getActivity()))
//                            .override(60, 60)
                            .into(Image_profile);
                }




            showShortToast(getActivity(), "Profile is up-to-date");


        }
        else
        {
            showLongToast(getActivity(), "Profile is not Updated");
        }
    }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.edit_profile:

                    // Replace edit frgament with animation
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                            .replace(R.id.profileContainer, new EditProfile(),
                                    Util.Edit_Profile).commit();
                    break;
            }

        }

}
