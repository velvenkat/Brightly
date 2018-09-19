package com.purplefront.brightly.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purplefront.brightly.API.ApiCallback;
import com.purplefront.brightly.API.RetrofitInterface;
import com.purplefront.brightly.Activities.MyChannel;
import com.purplefront.brightly.Adapters.NotificationsAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.NotificationsResponse;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications extends BaseFragment {

    View view;
    private static FragmentManager fragmentManager;
    List<NotificationsModel> notificationsModels = new ArrayList<>();
    NotificationsAdapter notificationsAdapter;
    TextView view_nodata;
    RecyclerView notifications_listview;

    Realm realm;
    RealmResults<RealmModel> realmModel;

    String user_ID;
    String message;

    public Notifications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_notifications, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        // Set title bar
        ((MyChannel) getActivity()).setActionBarTitle("Notifications");

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();

        initViews();
        getNotification();

        return view;
    }

    private void initViews() {

        view_nodata = (TextView) view.findViewById(R.id.view_nodata);
        notifications_listview = (RecyclerView) view.findViewById(R.id.notifications_listview);

        realmModel.load();
        for(RealmModel model:realmModel) {
            user_ID = model.getUser_Id();
        }
    }


    public void getNotification() {
        try {

            if (CheckNetworkConnection.isOnline(getActivity())) {
                showProgress();
                Call<NotificationsResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getNotifications(user_ID);
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
        notificationsAdapter = new NotificationsAdapter(getActivity(), notificationsModel, user_ID);
        notifications_listview.setAdapter(notificationsAdapter);

    }
}
