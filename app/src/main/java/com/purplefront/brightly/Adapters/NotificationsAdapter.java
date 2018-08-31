package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CircleTransform;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{

    List<NotificationsModel> notificationsModels;
    Activity context;
    LayoutInflater inflater;


    public NotificationsAdapter(FragmentActivity activity, List<NotificationsModel> notificationsModels) {

        this.context = activity;
        this.notificationsModels = notificationsModels;
        inflater = (LayoutInflater.from(context));
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.items_notification_lists, parent, false);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {

        NotificationsModel notificationsModel = notificationsModels.get(position);

        if (notificationsModel.getContent() != null) {
            holder.notification_content.setText(notificationsModel.getContent());
        }
        if (notificationsModel.getDate_time() != null) {
            holder.notification_dateTime.setText(notificationsModel.getDate_time());
        }

        if (!notificationsModel.getShared_user_profile_pic().isEmpty()) {

            Glide.with(context)
                    .load(notificationsModel.getShared_user_profile_pic())
                    .centerCrop()
                   .transform(new CircleTransform(context))
                    /*.override(50, 50)*/
                    .into(holder.notification_Image);

        } else {
            Glide.with(context)
                    .load(R.drawable.default_user_image)
                    .centerCrop()
                    .transform(new CircleTransform(context))
                    /*.override(50, 50)*/
                    .into(holder.notification_Image);
        }

    }

    @Override
    public int getItemCount() {
        return notificationsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notification_content;
        TextView notification_dateTime;
        ImageView notification_Image;

        public ViewHolder(View itemView) {
            super(itemView);

            notification_Image = itemView.findViewById(R.id.notification_Image);
            notification_content = itemView.findViewById(R.id.notification_content);
            notification_dateTime = itemView.findViewById(R.id.notification_dateTime);
        }
    }
}
