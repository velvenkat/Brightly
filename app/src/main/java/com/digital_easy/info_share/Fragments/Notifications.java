package com.digital_easy.info_share.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.NotificationsAdapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Modules.NotificationsResponse;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications extends BaseFragment implements NotificationsAdapter.MessagePassInterface {

    View view;
    private static FragmentManager fragmentManager;
    List<NotificationsModel> notificationsModels = new ArrayList<>();
    NotificationsAdapter notificationsAdapter;
    TextView view_nodata;
    RecyclerView notifications_listview;
    boolean isFirebaseNfy;

    /*Realm realm;
    RealmResults<RealmModel> realmModel;
*/
   // String user_ID;
    String message;
    RealmModel user_obj;

    public Notifications() {
        // Required empty public constructor
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_notifications, container, false);
        user_obj=((BrightlyNavigationActivity)getActivity()).getUserModel();
        fragmentManager = getActivity().getSupportFragmentManager();
        if(getArguments()!=null) {
            Bundle bundle = getArguments();
            isFirebaseNfy = bundle.getBoolean("isFireBaseNfy", false);
        }
        // Set title bar
        ((BrightlyNavigationActivity) getActivity()).setActionBarTitle("Notifications");
        //((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle("Hello");
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Noti_Cnt", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        setHasOptionsMenu(true);

        /*realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();*/

        initViews();
        getNotification();
        //view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                     if(!isFirebaseNfy)
                    ((BrightlyNavigationActivity) getActivity()).onFragmentBackKeyHandler(true);
                     else {

                         ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.CHANNELS, new ViewPager_Category(), false);
                     }
                }
                return true;
            }
        });

        return view;
    }

    private void initViews() {

        view_nodata = (TextView) view.findViewById(R.id.view_nodata);
        notifications_listview = (RecyclerView) view.findViewById(R.id.notifications_listview);

      //  realmModel.load();
       /* for(RealmModel model:realmModel) {
            user_ID = model.getUser_Id();
        }*/
    }


    public void getNotification() {
        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<NotificationsResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getNotifications(user_obj.getUser_Id());
                callRegisterUser.enqueue(new ApiCallback<NotificationsResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<NotificationsResponse> response, boolean isSuccess, String message) {
                        NotificationsResponse notificationsResponse = response.body();

                        if (isSuccess) {

                            if (notificationsResponse != null && notificationsResponse.getData() != null && notificationsResponse.getData().size() != 0) {

                                message = notificationsResponse.getMessage();
                                notificationsModels = notificationsResponse.getData();
                                setNotificationCredentials(notificationsModels, message);
                                notifications_listview.setVisibility(View.VISIBLE);
                                view_nodata.setVisibility(View.GONE);
                                dismissProgress();

                            } else {
                                notifications_listview.setVisibility(View.GONE);
                                view_nodata.setVisibility(View.VISIBLE);
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
     * @param notificationsModel
     */
    private void setNotificationCredentials(List<NotificationsModel> notificationsModel, String message ) {

        notifications_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationsAdapter = new NotificationsAdapter(this,getActivity(), notificationsModel, user_obj.getUser_Id());
        notifications_listview.setAdapter(notificationsAdapter);

    }

    @Override
    public void onMessagePass(Fragment fragment) {
        ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.view_card,fragment,false);
    }
}
