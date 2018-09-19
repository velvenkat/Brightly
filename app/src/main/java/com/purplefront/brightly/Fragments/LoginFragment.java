package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.Activities.MyChannel;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.SignInResponse;
import com.purplefront.brightly.Modules.SignUpResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.SharedPrefUtils;
import com.purplefront.brightly.Utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    private static FragmentManager fragmentManager;
    private EditText editText_phone;
    private EditText editText_password;
    private Button btn_signIn;
    private TextView textView_signUp, forgot_password;
    private static Animation shakeAnimation;

    String getPhonenumber;
    String getPassword;
    String User_ID;
    String UserName;
    String User_CompanyName;
    String User_Email;
    String phoneNumber,deviceToken;
    String image;

    Realm realm;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        realm = Realm.getDefaultInstance();
        initViews();
        setListeners();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                deviceToken = instanceIdResult.getToken();
                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
            }
        });

        return view;

    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        editText_phone = (EditText) view.findViewById(R.id.login_phonenumber);
        editText_password = (EditText) view.findViewById(R.id.login_password);
        btn_signIn = (Button) view.findViewById(R.id.loginBtn);
        textView_signUp = (TextView) view.findViewById(R.id.text_signup);
        forgot_password = (TextView) view.findViewById(R.id.forgot_password);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            textView_signUp.setTextColor(csl);
            forgot_password.setTextColor(csl);

        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        btn_signIn.setOnClickListener(this);
        textView_signUp.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.text_signup:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignupFragment(),
                                Util.SignUp_Fragment).commit();
                break;

            case R.id.forgot_password:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new ForgotPassword(),
                                Util.ForgotPassword_Fragment).commit();
                break;
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get phonenumber
        getPhonenumber = editText_phone.getText().toString();
        getPassword = editText_password.getText().toString();

        // Check for phonenumber field is empty or not
        if (getPhonenumber.equals("") || getPhonenumber.length() != 10 )
        {
            editText_phone.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter Valid Phone Number.");

        }
        else if( getPassword.equals("") || getPassword.length() == 0)
        {
            editText_password.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter Password.");
        }
            // Else do login and do your stuff
        else {
            getSignIn();
            //            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT).show();
        }

    }

    public void getSignIn() {
       // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                Call<SignInResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getSignIn(getPhonenumber, getPassword,deviceToken);
                callRegisterUser.enqueue(new ApiCallback<SignInResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<SignInResponse> response, boolean isSuccess, String message) {
                        SignInResponse signInResponse = response.body();

                        if (isSuccess) {

                            if (signInResponse != null) {

                                setLoginCredentials(signInResponse);
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
     * @param signInResponse
     */
    private void setLoginCredentials(SignInResponse signInResponse) {

        phoneNumber = signInResponse.getMobile();
        User_ID = signInResponse.getId();
        UserName  = signInResponse.getName();
        User_Email  = signInResponse.getEmail();
        User_CompanyName  = signInResponse.getCompany_name();
        image  = signInResponse.getImage();


        if(phoneNumber!=null) {

            realm.beginTransaction();
            RealmModel realmModel=realm.createObject(RealmModel.class);
            realmModel.setUser_Id(User_ID);
            realmModel.setUser_Name(UserName);
            realmModel.setUser_Email(User_Email);
            realmModel.setUser_PhoneNumber(phoneNumber);
            realmModel.setUser_CompanyName(User_CompanyName);
            realmModel.setImage(image);
            realm.commitTransaction();

            showLongToast(getActivity(), signInResponse.getMessage());
            getActivity().finish();
            frwdAnimIntent(getActivity(), MyChannel.class);


        }
        else
        {
            showLongToast(getActivity(), signInResponse.getMessage());
        }
    }
}
