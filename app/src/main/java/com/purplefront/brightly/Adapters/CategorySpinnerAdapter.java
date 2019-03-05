package com.purplefront.brightly.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.R;

import java.util.List;

public class CategorySpinnerAdapter extends BaseAdapter {

    List<ChannelListModel> channelListModelList;
    Context scrn_context;

    public CategorySpinnerAdapter(Context context, List<ChannelListModel> args) {
        channelListModelList = args;
        scrn_context = context;
    }

    @Override
    public int getCount() {
        return channelListModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(scrn_context);
            convertView = inflater.inflate(R.layout.items_spinner_catg, null);
        }
        return convertView;
    }
}
