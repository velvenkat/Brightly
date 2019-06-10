package com.digital_easy.info_share.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.digital_easy.info_share.Modules.GeneralVarModel;

import java.util.List;

public class ViewSetAdapter extends PagerAdapter {
    List<GeneralVarModel> filter_keys;

    public ViewSetAdapter(List<GeneralVarModel> filtr_key_value) {
        filter_keys = filtr_key_value;
    }

    @Override
    public int getCount() {
        return filter_keys.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
