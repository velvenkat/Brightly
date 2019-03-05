package com.purplefront.brightly.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Adapters.SetAdapterNew;
import com.purplefront.brightly.Adapters.ViewCardFragmentPagerAdapter;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Custom.CustomTabLayout;
import com.purplefront.brightly.Modules.GeneralVarModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class SetPagerFragment extends BaseFragment implements SetAdapterNew.Set_sel_interface {
    View rootView;
    RecyclerView recycle_set_list;
    List<SetsListModel> setModelListObj, temp_SetListObj;
    SetAdapterNew channelsSetAdapter;
    SetsFragment Parent_frag;

    String filter_key;
    RealmModel user_obj;
    String set_id_toCreateCard;
    ViewPager vw_Pgr_set;
    CustomTabLayout tab_set_type;
    List<GeneralVarModel> list_level2_filter_keys;
    ImageView imgCreateSet;
    ArrayList<String> type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lo_set_pager, container, false);
        vw_Pgr_set = (ViewPager) rootView.findViewById(R.id.viewpager_set);
        tab_set_type = (CustomTabLayout) rootView.findViewById(R.id.tabs_set_type);
        imgCreateSet=(ImageView)rootView.findViewById(R.id.image_createSet);
        list_level2_filter_keys = ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel2_filter();
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel2title().getPlural());
        type = new ArrayList<>();
        for (GeneralVarModel moduleObj : list_level2_filter_keys) {
            type.add(moduleObj.getPlural());
        }
        setup_view_pager();
        if (type.size() <= 3)
            tab_set_type.setDIVIDER_FACTOR(type.size());
        else {
            tab_set_type.setDIVIDER_FACTOR(3);
        }
        tab_set_type.initTabMinWidth();
        tab_set_type.invalidate();
        tab_set_type.setupWithViewPager(vw_Pgr_set);
        imgCreateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragCreate_Set=new CreateSet();
                Bundle bundle =new Bundle();
                bundle.putBoolean("isSetTop",true);
                fragCreate_Set.setArguments(bundle);
                ((BrightlyNavigationActivity)getActivity()).onFragmentCall(Util.Create_Set,fragCreate_Set,true);

            }
        });
        setup_tab();
        return rootView;
    }

    private void setup_tab() {
        for (int i = 0; i < type.size(); i++) {
            tab_set_type.getTabAt(i).setText(type.get(i));
            // tab_catg_type.setMinimumWidth(70);
        }
    }

    private void setup_view_pager() {

        List<Fragment> channel_type = new ArrayList<>();
        for (int i = 0; i < type.size(); i++) {
            Fragment card_frag = new SetsFragment();


            Bundle bundle = new Bundle();
            bundle.putString("filter_key", list_level2_filter_keys.get(i).getFetch_key());
            // bundle.putParcelable("model_obj", setsListModelList.get(i).getChl_model_obj());
            // bundle.putInt("filter_key", i);
            //bundle.putString("Set_ID_toCreateCard", set_id_toCreateCard);
            card_frag.setArguments(bundle);
            channel_type.add(card_frag);
        }
        ViewCardFragmentPagerAdapter adapter = new ViewCardFragmentPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), channel_type, null, null, null);

        vw_Pgr_set.setAdapter(adapter);

    }

    private void setAdapter(List<SetsListModel> setsListModels) {

        recycle_set_list.setLayoutManager(new LinearLayoutManager(getContext()));
        channelsSetAdapter = new SetAdapterNew(getContext(), setModelListObj, this);


        recycle_set_list.setAdapter(channelsSetAdapter);
        //  channelsSetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelect(int position, View view, boolean isShare) {
        Parent_frag.onSelect(position, null, isShare);
    }

    @Override
    public void onCardClick(int position) {
        Parent_frag.onCardClick(position);
    }
}
