package com.purplefront.brightly.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.CustomToast;
import com.purplefront.brightly.Modules.AddMessageResponse;
import com.purplefront.brightly.Modules.SetEntryModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;
import com.purplefront.brightly.Utils.Util;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileType extends BaseFragment {

    View frag_rootView;
   // ImageView image_file_link;
    EditText create_cardName;
    EditText create_cardDescription;
    EditText create_cardURL;
    Button btn_createCard;

    Context context;
    SetEntryModel setEntryModelObj;

    String userId;
    String set_id;
    String set_name;
    String card_name = "";
    String card_description = "";
    //    String youtube_url = "";
    String image_name;
    String type = "";
    RealmModel realmModelObj;

    boolean isCreateCard;


    public FileType() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frag_rootView = inflater.inflate(R.layout.fragment_file_type, container, false);
        realmModelObj=((BrightlyNavigationActivity)getActivity()).getUserModel();
        Bundle bundle=getArguments();
        setEntryModelObj=bundle.getParcelable("set_entry_obj");

        userId = realmModelObj.getUser_Id();
        set_id = setEntryModelObj.getSet_id();
        set_name = setEntryModelObj.getSet_name();

        create_cardURL = (EditText) frag_rootView.findViewById(R.id.create_cardURL);
        create_cardName = (EditText) frag_rootView.findViewById(R.id.create_cardName);
        create_cardDescription = (EditText) frag_rootView.findViewById(R.id.create_cardDescription);
        btn_createCard = (Button)frag_rootView.findViewById(R.id.btn_createCard);


        isCreateCard=bundle.getBoolean("isCreate");
        clear_edit_text_focus(create_cardDescription);
        clear_edit_text_focus(create_cardName);
        clear_edit_text_focus(create_cardURL);
        if(isCreateCard){

        }
        else{
            btn_createCard.setText("UPDATE CARD");
            create_cardName.setText(setEntryModelObj.getCard_name());
            create_cardDescription.setText(setEntryModelObj.getCard_description());
            if(setEntryModelObj.getType().equalsIgnoreCase("file"))
            create_cardURL.setText(setEntryModelObj.getCard_multimedia_url());
        }
        btn_createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        /*image_youtube_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });*/
        frag_rootView.setFocusableInTouchMode(true);
        frag_rootView.requestFocus();
        frag_rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {

                    ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
                }
                return true;
            }
        });


        return frag_rootView;
    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        card_name = create_cardName.getText().toString();
        card_description = create_cardDescription.getText().toString();

        String str = create_cardURL.getText().toString();
        String[] arrOfStr = str.split("http");
        if(arrOfStr.length == 1)
        {
            image_name = arrOfStr[0];
            image_name = "https://" + image_name;
        }
        else if(arrOfStr.length == 2) {
            image_name = arrOfStr[1];
            image_name = "http" + image_name;
        }
        // Check if all strings are null or not
        if (card_name.equals("") || card_name.length() == 0
                || card_description.equals("") || card_description.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), create_cardName,
                    "All fields are required.");
        }
        else if(image_name.equals("") || image_name.length() == 0)
        {
            new CustomToast().Show_Toast(getActivity(), create_cardURL,
                    "File link is required.");
        }

        else if(!Patterns.WEB_URL.matcher(image_name).matches())
        {
            new CustomToast().Show_Toast(getActivity(), create_cardURL,
                    "File link is Invalid.");
        }

        // Else do signup or do your stuff
        else {
            getAddCards();
        }
    }

    private void getAddCards() {

        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<AddMessageResponse> callRegisterUser;
                if(isCreateCard)
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getAddCardsList_call("file", userId, set_id, card_name, card_description, "", image_name );
                else
                    callRegisterUser = RetrofitInterface.getRestApiMethods(getActivity()).getUpdateCardsList_call("file", userId, set_id, setEntryModelObj.getCard_id(),card_name, card_description, "", image_name );
                callRegisterUser.enqueue(new ApiCallback<AddMessageResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AddMessageResponse> response, boolean isSuccess, String message) {
                        AddMessageResponse addMessageResponse = response.body();
                        if (isSuccess) {

                            if (addMessageResponse != null) {
                                dismissProgress();
                                setAddSetCredentials(addMessageResponse);


                            } else {
                                dismissProgress();

                            }

                        } else {

                            dismissProgress();
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

                        if(message.equals("timeout"))
                        {
                            showLongToast(getActivity(), "Internet is slow, please try again.");
                        }
                        dismissProgress();
                    }
                });
            } else {

                dismissProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();

            dismissProgress();
        }
    }


    private void setAddSetCredentials(AddMessageResponse addMessageResponse) {


        String message = addMessageResponse.getMessage();

        if(message.equals("success"))
        {
           /* Intent intent = new Intent(getActivity(), MySetCards.class);
            intent.putExtra("set_id", set_id);
            intent.putExtra("set_name", set_name);
            intent.putExtra("userId", userId);
            startActivity(intent);*/
          /*  getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.left_enter, R.anim.right_out);*/
            if(isCreateCard)
            {
                showShortToast(getActivity(), "Card "+card_name+" has been Created.");
            }
            else
            {
                showShortToast(getActivity(), "Card "+card_name+" has been Updated.");
            }
            ((BrightlyNavigationActivity)getActivity()).onFragmentBackKeyHandler(true);
        }

        else {
            showLongToast(getActivity(), message);
        }
    }


}
