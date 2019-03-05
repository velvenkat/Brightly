package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.AppVarModule;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.Modules.SignInResponse;
import com.purplefront.brightly.OTPService.OTPReaderCustom;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import swarajsaaj.smscodereader.interfaces.OTPListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, OTPListener, Login.PermissionResultInterface {

    private View view;
    private static FragmentManager fragmentManager;
    private EditText editText_phone;
    private Button btn_signIn;
    private TextView textView_signUp;
    private static Animation shakeAnimation;

    String getPhonenumber;
    String getPassword;
    String User_ID;
    String UserName;
    String User_CompanyName;
    String User_Email;
    String phoneNumber, deviceToken;
    String image;
    String message;
    String[] otp_msg;
    EditText edit_otp;
    Realm realm;
    String otp;
    String otp_resonse;
    AppVarModule appVarModuleObj;
    Bundle bundleArgs;
    CheckBox radio_otp;
    EditText edt_pwd;
    TextView txtForgotPassword;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        edt_pwd = (EditText) view.findViewById(R.id.logn_password);
        txtForgotPassword = view.findViewById(R.id.txtForgotPassword);
        realm = Realm.getDefaultInstance();
        initViews();
        setListeners();
        bundleArgs = getArguments();
        appVarModuleObj = bundleArgs.getParcelable("app_var_obj");
        radio_otp = view.findViewById(R.id.radio_login_otp);

        txtForgotPassword.setOnClickListener(this);
        radio_otp.setOnClickListener(this);

     //   requestRuntimePermissions();
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

        btn_signIn = (Button) view.findViewById(R.id.loginBtn);
        textView_signUp = (TextView) view.findViewById(R.id.text_signup);
        // forgot_password = (TextView) view.findViewById(R.id.forgot_password);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            textView_signUp.setTextColor(csl);
            //     forgot_password.setTextColor(csl);

        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        btn_signIn.setOnClickListener(this);
        textView_signUp.setOnClickListener(this);
        //forgot_password.setOnClickListener(this);
    }

    private void otpDialog() {
        // btn_signUp.setEnabled(true);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_otp, null);

        // OtpReader.bind(this, "611332");
        OTPReaderCustom.bind(this, "HP-Bright");

        edit_otp = (EditText) mView.findViewById(R.id.edit_otp);
        Button btn_Verify = (Button) mView.findViewById(R.id.btn_Verify);
        TextView text_resend = (TextView) mView.findViewById(R.id.text_resend);
        TextView resend_in = (TextView) mView.findViewById(R.id.resend_in);
        ImageView close_dialog = (ImageView) mView.findViewById(R.id.close_dialog);
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

                    if (otp.length() == 6) {
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

    private void setValidateOtp(SignInResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();
        message = addMessageResponse.getMessage();
        phoneNumber = addMessageResponse.getMobile();
        User_ID = addMessageResponse.getId();
        UserName = addMessageResponse.getName();
        User_Email = addMessageResponse.getEmail();
        User_CompanyName = addMessageResponse.getCompany_name();
        image = addMessageResponse.getImage();

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
            realmModel.setImage(image);
            realmModel.setUser_PhoneNumber(phoneNumber);
            realmModel.setUser_CompanyName(User_CompanyName);
            realm.commitTransaction();
            showLongToast(getActivity(), "Welcome " + UserName);
            // frwdAnimIntent(getActivity(), BrightlyNavigationActivity.class, realmModel);
            Intent intent = new Intent(getActivity(), BrightlyNavigationActivity.class);
            intent.putExtra("app_var_obj", appVarModuleObj);
            if (realmModel != null)
                intent.putExtra("user_obj", realmModel);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            getActivity().finish();

        } else {
            showLongToast(getActivity(), message);
        }
    }

    public void getValidateOtp() {
        // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<SignInResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getValidateOtp(getPhonenumber, otp);
                callRegisterUser.enqueue(new ApiCallback<SignInResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<SignInResponse> response, boolean isSuccess, String message) {
                        SignInResponse addMessageResponse = response.body();
                        dismissProgress();
                        if (isSuccess) {

                            if (addMessageResponse != null) {

                                setValidateOtp(addMessageResponse);


                            }

                        } else {
                            showLongToast(getActivity(), message);
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

    public void getResendOtp() {
        // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getResendOtp(getPhonenumber);
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
        otp_resonse = addMessageResponse.getOtp();
        if (message.equals("success")) {

            showLongToast(getActivity(), "OTP has been Sent");
        } else {
            showLongToast(getActivity(), message);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;
            case R.id.radio_login_otp:
                if (radio_otp.isChecked()) {
                    // radio_otp.setChecked(false);
                    edt_pwd.setVisibility(View.GONE);
                } else {
                    // radio_otp.setChecked(true);
                    edt_pwd.setVisibility(View.VISIBLE);

                }
                break;

            case R.id.text_signup:

                // Replace signup frgament with animation
                SignupFragment signup_frag = new SignupFragment();
                signup_frag.setArguments(bundleArgs);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .addToBackStack(Util.SignUp_Fragment)
                        .replace(R.id.frameContainer, signup_frag,
                                Util.SignUp_Fragment).commit();
                break;

            case R.id.txtForgotPassword:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .addToBackStack(Util.ForgotPassword_Fragment)
                        .replace(R.id.frameContainer, new ForgotPassword(),
                                Util.ForgotPassword_Fragment).commit();
                break;
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get phonenumber
        getPhonenumber = editText_phone.getText().toString();
        getPassword = edt_pwd.getText().toString();

        // Check for phonenumber field is empty or not
        if (getPhonenumber.trim().equals("") || getPhonenumber.trim().length() != 10) {
            editText_phone.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter Valid Phone Number.");

        } else if (!radio_otp.isChecked() && getPassword.trim().equals("")) {

            editText_phone.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Password should not be empty");
        }
       /* else if( getPassword.equals("") || getPassword.length() == 0)
        {
            editText_password.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter Password.");
        }*/
        // Else do login and do your stuff
        else {
            if (radio_otp.isChecked())
                getSignIn(true);
            else
                getSignIn(false);
            //            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT).show();
        }

    }

    public void getSignIn(boolean isOtptoLogin) {
        // String Token= FirebaseInstanceId.getInstance().getToken();
        Call<SignInResponse> callRegisterUser;
        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
               showProgress();
                if (isOtptoLogin)
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getSignIn(getPhonenumber, "", deviceToken, "android");
                else
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getSignIn(getPhonenumber, getPassword, deviceToken, "android");
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
                        dismissProgress();
                        showLongToast(getActivity(), message);
                    }
                });
            } else {
                dismissProgress();
                showLongToast(getActivity(), "Network Error");
            }
        } catch (Exception e) {
            dismissProgress();
            e.printStackTrace();
            showLongToast(getActivity(), "Something went Wrong, Please try Later");


        }
    }

    /**
     * @param signInResponse
     */
    private void setLoginCredentials(SignInResponse signInResponse) {

        message = signInResponse.getMessage();
        phoneNumber = signInResponse.getMobile();
        User_ID = signInResponse.getId();
        UserName = signInResponse.getName();
        User_Email = signInResponse.getEmail();
        User_CompanyName = signInResponse.getCompany_name();
        image = signInResponse.getImage();


        if (message.equals("success")) {




            /*frwdAnimIntent(getActivity(), BrightlyNavigationActivity.class, realmModel);
            showLongToast(getActivity(), "Welcome Again " + UserName);
            getActivity().finish();*/
            if (radio_otp.isChecked())
                otpDialog();
            else {
                showLongToast(getActivity(), "Welcome Again " + UserName);
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
                realmModel.setImage(image);
                realmModel.setUser_PhoneNumber(phoneNumber);
                realmModel.setUser_CompanyName(User_CompanyName);
                realm.commitTransaction();
                showLongToast(getActivity(), "Welcome " + UserName);
                // frwdAnimIntent(getActivity(), BrightlyNavigationActivity.class, realmModel);
                Intent intent = new Intent(getActivity(), BrightlyNavigationActivity.class);
                intent.putExtra("app_var_obj", appVarModuleObj);
                if (realmModel != null)
                    intent.putExtra("user_obj", realmModel);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                getActivity().finish();
            }

        } else {
            showLongToast(getActivity(), signInResponse.getMessage());
        }
    }

    @Override
    public void otpReceived(String messageText) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), "OTP : " + messageText, Toast.LENGTH_LONG).show();
        otp_msg = messageText.split("is");

        if (otp_msg != null) {
            edit_otp.setText("");
            edit_otp.setText(otp_msg[1].trim());
        }
    }

    @Override
    public void onPermissionResult_rcd(ArrayList<ContactShare> contact_list) {

    }

}
