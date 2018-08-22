package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.Activities.MyChannel;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.SignUpResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    Context mContext;
    private static FragmentManager fragmentManager;
    private EditText editText_name, editText_companyname, editText_email, editText_phone, reg_password, reg_confirmPassword;
    private Button btn_signUp;
    private TextView textView_signIn;
    private static Animation shakeAnimation;

    String getName;
    String getCompanyName;
    String getEmail;
    String getPhoneNumber;
    String getPassword;
    String getConfirmPassword;
    String message;

    Realm realm;

    String User_ID;
    String UserName;
    String User_CompanyName;
    String User_Email;
    String phoneNumber;



    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        realm = Realm.getDefaultInstance();
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        editText_name = (EditText) view.findViewById(R.id.registration_name);
        editText_companyname = (EditText) view.findViewById(R.id.registration_companyname);
        editText_email = (EditText) view.findViewById(R.id.registration_email);
        editText_phone = (EditText) view.findViewById(R.id.registration_phonenumber);
        reg_password = (EditText) view.findViewById(R.id.reg_password);
        reg_confirmPassword = (EditText) view.findViewById(R.id.reg_confirmPassword);

        btn_signUp = (Button) view.findViewById(R.id.signupBtn);
        textView_signIn = (TextView) view.findViewById(R.id.text_signin);


        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            textView_signIn.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        btn_signUp.setOnClickListener(this);
        textView_signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signupBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.text_signin:

                // Replace login fragment
                new Login().replaceLoginFragment();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        getName = editText_name.getText().toString();
        getCompanyName = editText_companyname.getText().toString();
        getEmail = editText_email.getText().toString();
        getPhoneNumber = editText_phone.getText().toString();
        getPassword = reg_password.getText().toString();
        getConfirmPassword = reg_confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Util.regEx);
        Matcher m = p.matcher(getEmail);

        // Check if all strings are null or not
        if (getName.equals("") || getName.length() == 0
                || getCompanyName.equals("") || getCompanyName.length() == 0
                || getEmail.equals("") || getEmail.length() == 0
                || getPhoneNumber.equals("") || getPhoneNumber.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");
        }

            // Check if email id valid or not
        else if (!m.find()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
        }
        else if (!getPassword.equals(getConfirmPassword))
        {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Password didn't match");
        }

            // Else do signup or do your stuff
        else
            getSignUp();
//            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();


    }

    public void getSignUp() {
        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                Call<SignUpResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getSignup(getName, getEmail, getPhoneNumber, getCompanyName, getPassword);
                callRegisterUser.enqueue(new ApiCallback<SignUpResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<SignUpResponse> response, boolean isSuccess, String message) {
                        SignUpResponse signUpResponse = response.body();

                        if (isSuccess) {

                            if (signUpResponse != null) {

                                setLoginCredentials(signUpResponse);
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
     * @param signUpResponse
     */
    private void setLoginCredentials(SignUpResponse signUpResponse) {

        /*Name = signInResponse.getName();
        companyName = signInResponse.getCompany_name();
        Email = signInResponse.getEmail();*/
        message = signUpResponse.getMessage();
        phoneNumber = signUpResponse.getMobile();
        User_ID = signUpResponse.getId();
        UserName  = signUpResponse.getName();
        User_Email  = signUpResponse.getEmail();
        User_CompanyName  = signUpResponse.getCompany_name();

        if(!message.equals("Unable to create User.")) {

            realm.beginTransaction();
            RealmModel realmModel=realm.createObject(RealmModel.class);
            realmModel.setUser_Id(User_ID);
            realmModel.setUser_Name(UserName);
            realmModel.setUser_Email(User_Email);
            realmModel.setUser_PhoneNumber(phoneNumber);
            realmModel.setUser_CompanyName(User_CompanyName);
            realm.commitTransaction();

            showLongToast(getActivity(), signUpResponse.getMessage());
            finishIntent(getActivity(), MyChannel.class);

        }
        else
        {
            showLongToast(getActivity(), getPhoneNumber+" is already Registered");
        }
    }
}
