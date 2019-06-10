package com.digital_easy.info_share.Fragments;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.digital_easy.info_share.API.ApiCallback;
import com.digital_easy.info_share.API.RetrofitInterface;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.CategoryGridAdapter;
import com.digital_easy.info_share.Adapters.MyChannelsAdapter;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.ChannelListResponse;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.CheckNetworkConnection;
import com.digital_easy.info_share.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Dashboard_Category extends Fragment implements MyChannelsAdapter.ChannelListItemClickListener {
    View rootView;
    LinearLayout scroll_categ;
    RealmModel user_obj;

    List<ChannelListModel> list_channel_model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dashboard_caregory, container, false);
        user_obj = ((BrightlyNavigationActivity) getActivity()).getUserModel();
        scroll_categ = rootView.findViewById(R.id.dash_catg_contr);
        getChannelsLists("all");
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");
        return rootView;
    }

    public void getChannelsLists(String type) {
        try {

            if (CheckNetworkConnection.isOnline(getContext())) {

                Call<ChannelListResponse> callRegisterUser = RetrofitInterface.getRestApiMethods(getContext()).getMyChannelsList(user_obj.getUser_Id(), type);
                callRegisterUser.enqueue(new ApiCallback<ChannelListResponse>(getActivity()) {
                    @Override
                    public void onApiResponse(Response<ChannelListResponse> response, boolean isSuccess, String message) {
                        ChannelListResponse channelListResponse = response.body();

                        if (isSuccess) {

                            if (channelListResponse != null && channelListResponse.getChannels() != null && channelListResponse.getChannels().size() != 0) {
                                //  swipeRefresh.setRefreshing(true);
                                list_channel_model = channelListResponse.getChannels();
                                for (int i = 0; i < list_channel_model.size(); i = i + 2) {
                                    setAdapter(i, "Recent Categories ");
                                }

                            } else {
                                // swipeRefresh.setRefreshing(false);

                            }

                        } else {


                            //     swipeRefresh.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onApiFailure(boolean isSuccess, String message) {


                    }
                });
            } else {
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_LONG).show();

              /*  else
                    dismissProgress();*/
            }
        } catch (Exception e) {
            e.printStackTrace();

            //swipeRefresh.setRefreshing(true);
        }


    }

    private void setAdapter(int Position, String hdr_text) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View Hdr_vw = inflater.inflate(R.layout.lo_row_dash_hdr, null);
        TextView txt_see_all = Hdr_vw.findViewById(R.id.txt_see_all);
        scroll_categ.addView(Hdr_vw);
        TextView txt_hdr = Hdr_vw.findViewById(R.id.txt_hdr);
        txt_hdr.setText(hdr_text);
        txt_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewPager_Category();

                Bundle bundle = new Bundle();
                bundle.putString("type", "all");
                fragment.setArguments(bundle);
            /*fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.frag_container, new ChannelFragment(),
                            Util.CHANNELS).commit();*/
                ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.CHANNELS, fragment, false);
            }
        });
        RecyclerView grid_chl_items = Hdr_vw.findViewById(R.id.grid_chl_itms);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        grid_chl_items.setLayoutManager(gridLayoutManager);
        MyChannelsAdapter adapter = new MyChannelsAdapter(getContext(), true, list_channel_model, this, width, Position, user_obj.getUser_Id(), ((BrightlyNavigationActivity) getActivity()).CATG_DEF_IMAGE);
        grid_chl_items.setAdapter(adapter);

    }

    @Override
    public void OnChannelItemClick(Fragment frag_args, ChannelListModel chl_list_obj) {
        Bundle bundle = new Bundle();


        ((BrightlyNavigationActivity) getActivity()).glob_chl_list_obj = chl_list_obj;
        // frag_args.setArguments(bundle);
        ((BrightlyNavigationActivity) getActivity()).onFragmentCall(Util.Set_List, frag_args, false);
    }
}
