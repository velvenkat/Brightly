package com.digital_easy.info_share.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.ListModuleAdapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.AdminSettingsModel;
import com.digital_easy.info_share.Modules.AdminSettingsModuleData;
import com.digital_easy.info_share.Modules.CardsListResponse;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Response;

public class ListModules extends BaseFragment {
    View rootView;
    RecyclerView recycle_list_module;
    RealmModel user_obj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_list_module, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        recycle_list_module = rootView.findViewById(R.id.recycle_list_modules);

        getModelsLists();
        return rootView;

    }

    public void getModelsLists() {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {
                showProgress();

                Call<AdminSettingsModel> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).list_custom_label(user_obj.getUser_Id());
                callRegisterUser.enqueue(new ApiCallback<AdminSettingsModel>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<AdminSettingsModel> response, boolean isSuccess, String message) {
                        dismissProgress();

                        AdminSettingsModel adminSettingsModelObj = response.body();
                        if (isSuccess) {
                            if (adminSettingsModelObj.getMessage().equalsIgnoreCase("success")) {
                                if (adminSettingsModelObj.getSettings_data().size() > 0)
                                    set_admin_set_adaptr(adminSettingsModelObj.getSettings_data());
                            }
                        }

                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {

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

    private void set_admin_set_adaptr(List<AdminSettingsModuleData> data_obj) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_list_module.setLayoutManager(gridLayoutManager);
        ListModuleAdapter adapter = new ListModuleAdapter(getContext(), data_obj);
        recycle_list_module.setAdapter(adapter);

    }
}
