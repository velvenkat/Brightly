package com.purplefront.brightly.Fragments;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.Util;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends BaseFragment implements View.OnClickListener{

    View view;
    private static FragmentManager fragmentManager;
    private EditText input_email, input_company, input_phone;
    private ImageView Image_profile;
    private TextView User_Name, User_Email;
    Realm realm;


    String User_ID;
    String UserName;
    String UserCompanyName;
    String UserEmail;
    String phoneNumber;

    public MyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        realm = Realm.getDefaultInstance();
        initViews();
        return view;
    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        input_email = (EditText) view.findViewById(R.id.input_email);
        input_company = (EditText) view.findViewById(R.id.input_company);
        input_phone = (EditText) view.findViewById(R.id.input_phone);
        User_Name = (TextView) view.findViewById(R.id.User_Name);
        User_Email = (TextView) view.findViewById(R.id.User_Email);
    }


        @Override
        public void onClick(View v) {

            /*switch (v.getId()) {

                case R.id.text_signup:

                    // Replace edit frgament with animation
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                            .replace(R.id.frameContainer, new EditProfile(),
                                    Util.Edit_Profile).commit();
                    break;
            }*/

        }

}
