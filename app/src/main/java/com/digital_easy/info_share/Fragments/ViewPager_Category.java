package com.digital_easy.info_share.Fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.ViewCardFragmentPagerAdapter;
import com.digital_easy.info_share.Custom.CustomTabLayout;
import com.digital_easy.info_share.Modules.GeneralVarModel;
import com.digital_easy.info_share.R;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class ViewPager_Category extends Fragment {
    View rootView;
    CustomTabLayout tab_catg_type;
    ViewPager vwPagr_chl;
    String Set_ID_toCreateCard;
    ArrayList<String> type;
    List<GeneralVarModel> catg_filter_keys;
    boolean isDashboard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.lo_vwpgr_catg, container, false);

        tab_catg_type = (CustomTabLayout) rootView.findViewById(R.id.tabs_catg_type);
        vwPagr_chl = (ViewPager) rootView.findViewById(R.id.viewpager_chl);
        boolean dontrun = ((BrightlyNavigationActivity) getActivity()).DontRun;
        ((BrightlyNavigationActivity) getActivity()).DontRunOneTime = false;
        ((BrightlyNavigationActivity) getActivity()).setActionBarTitle(null);
        if (getArguments() != null) {

        } else
            ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle(((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel2title().getPlural());
        ((BrightlyNavigationActivity) getActivity()).setActionBarTitle(((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel1title().getPlural());
        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        if (!dontrun) {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                isDashboard = bundle.getBoolean("isDashboard");
                Set_ID_toCreateCard = bundle.getString("Set_ID_toCreateCard");
                if (Set_ID_toCreateCard != null) {
                    ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setSubtitle("Select " + ((BrightlyNavigationActivity) getActivity()).CATEGORY_PLURAL + " to import " + ((BrightlyNavigationActivity) getActivity()).CARD_SINGULAR);
                }
            }
            type = new ArrayList<>();
            catg_filter_keys = ((BrightlyNavigationActivity) getActivity()).appVarModuleObj.getLevel1_filter();

            for (GeneralVarModel moduleObj : catg_filter_keys) {

                type.add(moduleObj.getPlural());
            }
            // type.add("Ascending order");
            // type.add("finish DATE");
            setup_view_pager();
            if (type.size() <= 3)
                tab_catg_type.setDIVIDER_FACTOR(type.size());
            else {
                tab_catg_type.setDIVIDER_FACTOR(3);
            }
            tab_catg_type.initTabMinWidth();
            tab_catg_type.invalidate();
            tab_catg_type.setupWithViewPager(vwPagr_chl);
            setup_tab();

        }
        return rootView;

    }

    private void setup_tab() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        for (int i = 0; i < type.size(); i++) {
            tab_catg_type.getTabAt(i).setText(type.get(i));

            // tab_catg_type.setMinimumWidth(70);
        }
        /*try {
            Field txtTab = ((TabLayout)tab_catg_type.getClass()).getDeclaredField("mScrollableTabMinWidth");
            txtTab.setAccessible(true);
            txtTab.set(this, (int) (size.x / (float) type.size()));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
*/
    }


    private void setup_view_pager() {

        List<Fragment> channel_type = new ArrayList<>();
        for (int i = 0; i < type.size(); i++) {
            Fragment card_frag = new ChannelFragment();

            if (i <= 2) {
                Bundle bundle = new Bundle();
                bundle.putString("type", catg_filter_keys.get(i).getFetch_key());
                bundle.putBoolean("isDashboard", isDashboard);
                bundle.putString("Set_ID_toCreateCard", Set_ID_toCreateCard);
                card_frag.setArguments(bundle);
            }
            channel_type.add(card_frag);

        }
        ViewCardFragmentPagerAdapter adapter = new ViewCardFragmentPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), channel_type, null, null, null);

        vwPagr_chl.setAdapter(adapter);
    }

}
