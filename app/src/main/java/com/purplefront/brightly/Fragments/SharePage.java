package com.purplefront.brightly.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.Util;

import javax.annotation.Nullable;

public class SharePage extends BaseFragment {

    Button share_inApp;
    Button share_aLink;

    String set_description = "";
    String set_name = "";
    String userId;
    String set_id = "";
    String share_link;
    ChannelListModel chl_list_obj;
    SetsListModel setsListModel;
    NotificationsModel notificationsModel;
    boolean isNotification;
    RealmModel user_obj;
    View rootView;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Share Set");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_share_page, container, false);
        ((BrightlyNavigationActivity) getActivity()).DisableBackBtn = false;
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();

        // setContentView(R.layout.activity_share_page);

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
*/
        // setTitle("Share Set");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Share Set");
        Bundle bundle = getArguments();

        if (bundle != null) {
            isNotification = bundle.getBoolean("isNotification", false);
        }

        if (isNotification) {
            notificationsModel = bundle.getParcelable("notfy_modl_obj");
            set_description = notificationsModel.getNotificationsSetDetail().getDescription();
            set_name = notificationsModel.getNotificationsSetDetail().getName();
            set_id = notificationsModel.getNotificationsSetDetail().getSet_id();
            share_link = notificationsModel.getNotificationsSetDetail().getShare_link();
        } else {

            chl_list_obj = bundle.getParcelable("model_obj");
            setsListModel = bundle.getParcelable("setsListModel");
            set_description = setsListModel.getDescription();
            set_name = setsListModel.getSet_name();
            set_id = setsListModel.getSet_id();
            share_link = setsListModel.getShare_link();
        }

        userId = user_obj.getUser_Id();

        share_inApp = (Button) rootView.findViewById(R.id.share_inApp);
        share_aLink = (Button) rootView.findViewById(R.id.share_aLink);

        share_inApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent intent = new Intent(SharePage.this, ShareWithContacts.class);
                intent.putExtra("userId", userId);
                intent.putExtra("model_obj", chl_list_obj);
                intent.putExtra("setsListModel", setsListModel);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                Fragment fragment = new ShareWithContacts();
                Bundle bundle1 = new Bundle();
                if (isNotification) {
                    bundle1.putBoolean("isNotification", true);
                    bundle1.putParcelable("notfy_modl_obj", notificationsModel);
                } else {
                    bundle1.putBoolean("isNotification", false);
                    bundle1.putParcelable("model_obj", chl_list_obj);
                    bundle1.putParcelable("setsListModel", setsListModel);
                }
                fragment.setArguments(bundle1);
                ((BrightlyNavigationActivity) getActivity()).isHide_frag = true;
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.share_with_contact, fragment, true);

            }
        });

        share_aLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Brightly Set Share link");
                share.putExtra(Intent.EXTRA_TEXT, share_link);

                startActivity(Intent.createChooser(share, "Share link!"));
            }
        });
        return rootView;
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }*/
}
