package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.Fragments.CardDetailFragment;
import com.purplefront.brightly.Modules.NotificationsModel;
import com.purplefront.brightly.Modules.NotificationsSetDetail;
import com.purplefront.brightly.R;
import com.purplefront.brightly.Utils.CircleTransform;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{

    List<NotificationsModel> notificationsModels;
    Activity context;
    LayoutInflater inflater;

    NotificationsSetDetail notificationsSetDetail;

    String userId;
    String channel_id = "";
    MessagePassInterface mListener;


    public NotificationsAdapter(MessagePassInterface listener,FragmentActivity activity, List<NotificationsModel> notificationsModels, String user_ID) {

        this.context = activity;
        this.userId = user_ID;
        this.notificationsModels = notificationsModels;
        inflater = (LayoutInflater.from(context));
        mListener=listener;
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
        channel_id = notificationsModel.getChannel_id();

        if (notificationsModel.getContent() != null) {
            holder.notification_content.setText(notificationsModel.getContent());
        }

        if(notificationsModel.getAction().equals("deleted")  || notificationsModel.getAction().equals("revoked")) {

            holder.notification_content.setTextColor(context.getResources().getColor(R.color.orange_color));
        }
        else
        {
            holder.notification_content.setTextColor(context.getResources().getColor(R.color.black));
        }


        if (notificationsModel.getDate_time() != null) {
            holder.notification_dateTime.setText(notificationsModel.getDate_time());
        }

        if (!notificationsModel.getShared_user_profile_pic().isEmpty()) {

            Glide.with(context)
                    .load(notificationsModel.getShared_user_profile_pic())
                    .placeholder(R.drawable.progress_icon)
                    .fitCenter()
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

        notificationsSetDetail = notificationsModel.getNotificationsSetDetail();

        holder.notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((notificationsModel.getType().equals("set")) && notificationsModel.getAction().equals("deleted")) {

                    Toast.makeText(context, "This Set is Deleted...", Toast.LENGTH_SHORT).show();

                }
                else if((notificationsModel.getType().equals("card")) && notificationsModel.getAction().equals("deleted")) {

                    Toast.makeText(context, "This Card is Deleted...", Toast.LENGTH_SHORT).show();

                }
                else if(notificationsModel.getAction().equals("revoked"))
                {
                    Toast.makeText(context, notificationsModel.getNotificationsSetDetail().getName() + " set permission has been Revoked.", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    /*Intent intent = new Intent(context, CardDetailFragment.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("notfy_modl_obj", notificationsModel);
                    intent.putExtra("isNotification", true);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                    Fragment fragment=new CardDetailFragment();
                    Bundle bundle =new Bundle();
                    bundle.putParcelable("notfy_modl_obj", notificationsModel);
                    bundle.putBoolean("isNotification", true);
                    fragment.setArguments(bundle);
                    mListener.onMessagePass(fragment);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notification_content;
        TextView notification_dateTime;
        ImageView notification_Image;
        RelativeLayout notification_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            notification_Image = itemView.findViewById(R.id.notification_Image);
            notification_content = itemView.findViewById(R.id.notification_content);
            notification_dateTime = itemView.findViewById(R.id.notification_dateTime);
            notification_layout = itemView.findViewById(R.id.notification_layout);
        }
    }

    public interface MessagePassInterface{
        public void onMessagePass(Fragment fragment);
    }
}
