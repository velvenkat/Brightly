package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.Login;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPassword extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText emailId;
    private TextView submit, back;

    String getEmailId;
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
        emailId = (EditText) view.findViewById(R.id.registered_emailid);
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
                new Login().replaceLoginFragment();
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

        }

    }

    private void submitButtonTask() {

        showProgress();
        getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Util.regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter your Email Id.");

        }
            // Check if email id is valid or not
        else if (!m.find()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
        }
            // Else submit email id and fetch passwod or do your stuff
        else {
            getForgotPassword();
         /*   Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();*/
        }
    }


    public void  getForgotPassword() {
        // String Token= FirebaseInstanceId.getInstance().getToken();

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                Call<AddMessageResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getForgetPassword(getEmailId);
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();

                        if (isSuccess) {

                            if (addMessageResponse != null) {

                                setForgotPassword(addMessageResponse);
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
                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showLongToast(getActivity(), "Something went Wrong, Please try Later");


        }
    }

    private void setForgotPassword(AddMessageResponse addMessageResponse) {

        String message = addMessageResponse.getMessage();

        if(message.equals("success"))
        {
            showLongToast(getActivity(), "Login Credentials are sent to your Email.");
            new Login().replaceLoginFragment();
           dismissProgress();
        }
        else {
            showLongToast(getActivity(), message);
        }
    }


}
