package com.purplefront.brightly.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.SignUpResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends BaseFragment implements View.OnClickListener, OTPListener {


    private static final int PERMISSION_REQUEST_ID = 100;
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
    String otp_msg;
    String otp_resonse;

    Realm realm;

    String User_ID;
    String UserName;
    String User_CompanyName;
    String User_Email;
    String phoneNumber;
    String deviceToken;

    EditText edit_otp;
    Button btn_Verify;
    TextView text_resend, resend_in;
    ImageView close_dialog;
    String otp;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        realm = Realm.getDefaultInstance();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        requestRuntimePermissions(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS);

        initViews();
        setListeners();
        //final String[] deviceToken = new String[1];
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

            btn_signUp.setClickable(true);

        }

        // Check if email id valid or not
        else if (!m.find()) {
            editText_email.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            btn_signUp.setEnabled(true);
        }

        // Check for phonenumber field is empty or not
        else if (getPhoneNumber.equals("") || getPhoneNumber.length() != 10) {
            editText_phone.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter Valid Phone Number.");

            btn_signUp.setEnabled(true);
        } else if (!getPassword.equals(getConfirmPassword)) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Password didn't match");
            btn_signUp.setEnabled(true);
        }

        // Else do signup or do your stuff
        else {
            btn_signUp.setEnabled(false);
            getSignUp();

//            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
        }

    }

    public void getSignUp() {
        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<SignUpResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getSignup(getName, getEmail, getPhoneNumber, getCompanyName, getPassword, deviceToken, "android");
                callRegisterUser.enqueue(new ApiCallback<SignUpResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<SignUpResponse> response, boolean isSuccess, String message) {
                        SignUpResponse signUpResponse = response.body();
                        btn_signUp.setEnabled(true);
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
                        dismissProgress();
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

    private void otpDialog() {
        btn_signUp.setEnabled(true);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_otp, null);

        OtpReader.bind(this, "611332");

        edit_otp = (EditText) mView.findViewById(R.id.edit_otp);
        btn_Verify = (Button) mView.findViewById(R.id.btn_Verify);
        text_resend = (TextView) mView.findViewById(R.id.text_resend);
        resend_in = (TextView) mView.findViewById(R.id.resend_in);
        close_dialog = (ImageView) mView.findViewById(R.id.close_dialog);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);


        final CountDownTimer timer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                text_resend.setEnabled(false);
                text_resend.setText(+millisUntilFinished / 1000 + " sec");
                text_resend.setTextSize(22);
                resend_in.setText("resend OTP in");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                text_resend.setTextSize(18);
                text_resend.setText("Resend OTP");
                text_resend.setEnabled(true);
                resend_in.setText("Or");
            }


        };
        timer.start();

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                timer.cancel();
                timer.onFinish();

            }
        });

        text_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResendOtp();

                final CountDownTimer timer = new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {

                        text_resend.setEnabled(false);
                        text_resend.setText(+millisUntilFinished / 1000 + " sec");
                        text_resend.setTextSize(22);
                        resend_in.setText("resend OTP in");
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        text_resend.setTextSize(18);
                        text_resend.setText("Resend OTP");
                        text_resend.setEnabled(true);
                        resend_in.setText("Or");
                    }


                };
                timer.start();
            }
        });

        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!edit_otp.getText().toString().isEmpty()) {

                    otp = edit_otp.getText().toString();

                    if (otp_resonse.length() == 6) {
                        getValidateOtp();
                       /* dialog.dismiss();
                        timer.cancel();
                        timer.onFinish();*/
                    } else {
                        Toast.makeText(getActivity(), "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    }

                    //method
//                    Toast.makeText(MainActivity.this, R.string.success_login_msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void requestRuntimePermissions(String... permissions) {
        for (String perm : permissions) {

            if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{perm}, PERMISSION_REQUEST_ID);

            }
        }
    }


    /**
     * @param signUpResponse
     */
    private void setLoginCredentials(SignUpResponse signUpResponse) {

        /*Name = signInResponse.getName();
        companyName = signInResponse.getCompany_name();
        Email = signInResponse.getEmail();*/

        otp_resonse = signUpResponse.getOtp();
        message = signUpResponse.getMessage();
        phoneNumber = signUpResponse.getMobile();
        User_ID = signUpResponse.getId();
        UserName = signUpResponse.getName();
        User_Email = signUpResponse.getEmail();
        User_CompanyName = signUpResponse.getCompany_name();

        if (message.equals("success")) {

            otpDialog();

        } else {
            showLongToast(getActivity(), signUpResponse.getMessage());
        }
    }

    public void getResendOtp() {
        // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getResendOtp(User_ID, phoneNumber);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();

                        if (isSuccess) {

                            if (addMessageResponse != null) {

                                setResendOtp(addMessageResponse);
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
//                        showLongToast(getActivity(), message);
                        dismissProgress();
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

    private void setResendOtp(AddMessageResponse addMessageResponse) {

        String message = addMessageResponse.getMessage();
        String otp_resonse = addMessageResponse.getOtp();
        if (message.equals("success")) {

            showLongToast(getActivity(), "OTP has been Sent");
        } else {
            showLongToast(getActivity(), message);
        }
    }


    public void getValidateOtp() {
        // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getValidateOtp(User_ID, otp);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        dismissProgress();
                        if (isSuccess) {

                            if (addMessageResponse != null) {

                                setValidateOtp(addMessageResponse);


                            }

                        }

                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {
                        dismissProgress();
                        showLongToast(getActivity(), message);

                    }
                });
            } else {
                dismissProgress();
                showLongToast(getActivity(), "Network Error");

            }
        } catch (Exception e) {
            e.printStackTrace();
            dismissProgress();
            showLongToast(getActivity(), "Something went Wrong, Please try Later");


        }
    }

    private void setValidateOtp(AddMessageResponse addMessageResponse) {

        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {
            realm.beginTransaction();
            realm.deleteAll();

            //commit realm changes
            realm.commitTransaction();
            realm.beginTransaction();
            RealmModel realmModel = realm.createObject(RealmModel.class);
            realmModel.setDeviceToken(deviceToken);
            realmModel.setUser_Id(User_ID);
            realmModel.setUser_Name(UserName);
            realmModel.setUser_Email(User_Email);
            realmModel.setUser_PhoneNumber(phoneNumber);
            realmModel.setUser_CompanyName(User_CompanyName);
            realm.commitTransaction();
            showLongToast(getActivity(), "Welcome " + UserName);
            frwdAnimIntent(getActivity(), BrightlyNavigationActivity.class, realmModel);

            getActivity().finish();

        } else {
            showLongToast(getActivity(), message);
        }
    }

    @Override
    public void otpReceived(String messageText) {

        Toast.makeText(getActivity(), "OTP : " + messageText, Toast.LENGTH_LONG).show();
        otp_msg = messageText;
        if (otp_msg != null) {
            edit_otp.setText("");
            edit_otp.setText(otp_msg);
        }

    }
}
