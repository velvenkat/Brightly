package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.OTPService.OTPReaderCustom;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import retrofit2.Call;
import retrofit2.Response;
import swarajsaaj.smscodereader.interfaces.OTPListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassword extends BaseFragment implements View.OnClickListener, OTPListener {

    private View view;
    //  private EditText emailId;
    private TextView submit, back;
    AlertDialog dialog;
    EditText new_passwd, confirm_passwd, edt_phoneno, edit_otp;
    // String getEmailId;
    String password, mob_no, otp, otp_msg[];
    Button btn_Verify;


    public ForgotPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        initViews();
        setListeners();
        return view;
    }

    // Initialize the views
    private void initViews() {
        new_passwd = (EditText) view.findViewById(R.id.new_passwd);
        edt_phoneno = view.findViewById(R.id.edt_phoneno);
        confirm_passwd = view.findViewById(R.id.confirm_password);
        submit = (TextView) view.findViewById(R.id.forgot_button);
        back = (TextView) view.findViewById(R.id.backToLoginBtn);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
        }

    }

    // Set Listeners over buttons
    private void setListeners() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLoginBtn:

                // Replace Login Fragment on Back Presses
                //  new Login().replaceLoginFragment();
                ((Login) getActivity()).getSupportFragmentManager().popBackStackImmediate();
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

        }

    }

    private void submitButtonTask() {


        password = new_passwd.getText().toString();
        String confrm_passwd = confirm_passwd.getText().toString();
        mob_no = edt_phoneno.getText().toString();
        // Pattern for email id validation

        if (password.trim().equals("") || confrm_passwd.trim().equals("") || mob_no.trim().equals("")) {

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required");
            if (mob_no.trim().equals(""))
                edt_phoneno.requestFocus();
            else if (password.trim().equals(""))
                new_passwd.requestFocus();
            else if (confrm_passwd.equals(""))
                confirm_passwd.requestFocus();

        } else if (mob_no.trim().length() < 10) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter valid mobile no");
        } else if (!password.equals(confrm_passwd)) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Password mismatch");
        }
        // Check if email id is valid or not

        // Else submit email id and fetch passwod or do your stuff
        else {
            getForgotPassword();
         /*   Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();*/
        }
    }

    private void otpDialog() {
        // btn_signUp.setEnabled(true);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_otp, null);

        // OtpReader.bind(this, "611332");
        OTPReaderCustom.bind(this, "HP-Bright");

        edit_otp = (EditText) mView.findViewById(R.id.edit_otp);
        btn_Verify = (Button) mView.findViewById(R.id.btn_Verify);
        TextView text_resend = (TextView) mView.findViewById(R.id.text_resend);
        TextView resend_in = (TextView) mView.findViewById(R.id.resend_in);
        ImageView close_dialog = (ImageView) mView.findViewById(R.id.close_dialog);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        dialog = mBuilder.create();
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

    public void getForgotPassword() {
        // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getForgetPassword_1(mob_no);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();

                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                dismissProgress();
                                setForgotPassword(addMessageResponse);


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
                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showLongToast(getActivity(), "Something went Wrong, Please try Later");


        }
    }

    private void setResendOtp(AddMessageResponse addMessageResponse) {

        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {

            showLongToast(getActivity(), "OTP has been Sent");
        } else {
            showLongToast(getActivity(), message);
        }
    }

    public void getResendOtp() {
        // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getResendOtp(mob_no);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();

                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                dismissProgress();
                                setResendOtp(addMessageResponse);


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

    private void setForgotPassword(AddMessageResponse addMessageResponse) {

        String message = addMessageResponse.getMessage();

        if (message.equals("success")) {
            // showLongToast(getActivity(), "Login Credentials are sent to your Email.");
            //  new Login().replaceLoginFragment();
            dismissProgress();
            otpDialog();
        } else {
            showLongToast(getActivity(), message);
        }
    }

    public void getValidateOtp() {
        // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getForgetPassword_2(mob_no, otp, password);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
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

    private void setValidateOtp(AddMessageResponse addMessageResponse) {

        ;
        String message = addMessageResponse.getMessage();
        dialog.dismiss();
        if (message.equalsIgnoreCase("success")) {
            Toast.makeText(getContext(),"Password reset successfully",Toast.LENGTH_LONG).show();
            ((Login) getActivity()).getSupportFragmentManager().popBackStackImmediate();
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
}
