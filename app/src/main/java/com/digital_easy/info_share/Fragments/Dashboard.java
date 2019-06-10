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

import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.ViewCardFragmentPagerAdapter;
import com.digital_easy.info_share.Custom.CustomTabLayout;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {
    View rootView;
    CustomTabLayout tabs_dash_type;
    ViewPager vwPagr_dash;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dashboard, container, false);
        tabs_dash_type = (CustomTabLayout) rootView.findViewById(R.id.tabs_dash_type);
        vwPagr_dash = (ViewPager) rootView.findViewById(R.id.viewpager_dash);
        setup_view_pager();

        ((BrightlyNavigationActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");

       /* if (type.size() <= 3)
            tab_catg_type.setDIVIDER_FACTOR(type.size());
        else {
            tab_catg_type.setDIVIDER_FACTOR(3);
        }*/
        tabs_dash_type.setDIVIDER_FACTOR(2);
//        tabs_dash_type.initTabMinWidth();
        tabs_dash_type.invalidate();
        tabs_dash_type.setupWithViewPager(vwPagr_dash);
        setup_tab();


        return rootView;

    }

    private void setup_tab() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

      /*  for (int i = 0; i < type.size(); i++) {
            tab_catg_type.getTabAt(i).setText(type.get(i));

            // tab_catg_type.setMinimumWidth(70);
        }*/
        tabs_dash_type.getTabAt(0).setText(((BrightlyNavigationActivity) getActivity()).CATEGORY_PLURAL + " VIEW");
        tabs_dash_type.getTabAt(1).setText(((BrightlyNavigationActivity) getActivity()).SET_PLURAL + " VIEW");
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

        List<Fragment> _type = new ArrayList<>();

        Fragment fragment = new Dashboard_Category();

        Bundle bundle = new Bundle();
        // bundle.putString("type", "all");
        bundle.putBoolean("isDashboard", true);
        fragment.setArguments(bundle);
        _type.add(fragment);

        Fragment fragment_2 = new SetPagerFragment();
        //bundle.putBoolean("isDashboard", true);
        fragment_2.setArguments(bundle);
        _type.add(fragment_2);


        ViewCardFragmentPagerAdapter adapter = new ViewCardFragmentPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), _type, null, null, null);

        vwPagr_dash.setAdapter(adapter);
    }

}
