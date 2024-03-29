package com.digital_easy.info_share.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.digital_easy.info_share.Fragments.CardDetailFragment;
import com.digital_easy.info_share.Fragments.CommentsFragment;
import com.digital_easy.info_share.Modules.NotificationsModel;
import com.digital_easy.info_share.Modules.NotificationsSetDetail;
import com.digital_easy.info_share.R;


import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    List<NotificationsModel> notificationsModels;
    Activity context;
    LayoutInflater inflater;

    NotificationsSetDetail notificationsSetDetail;

    String userId;
    String channel_id = "";
    MessagePassInterface mListener;
    ResizeOptions mResizeOptions = new ResizeOptions(120, 100);


    public NotificationsAdapter(MessagePassInterface listener, FragmentActivity activity, List<NotificationsModel> notificationsModels, String user_ID) {

        this.context = activity;
        this.userId = user_ID;
        this.notificationsModels = notificationsModels;
        inflater = (LayoutInflater.from(context));
        mListener = listener;
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

        if (notificationsModel.getAction().equals("deleted") || notificationsModel.getAction().equals("revoked")) {

            holder.notification_content.setTextColor(context.getResources().getColor(R.color.orange_color));
        } else {
            holder.notification_content.setTextColor(context.getResources().getColor(R.color.black));
        }


        if (notificationsModel.getDate_time() != null) {
            holder.notification_dateTime.setText(notificationsModel.getDate_time());
        }

        if (!notificationsModel.getShared_user_profile_pic().isEmpty()) {

            /*Glide.with(context)
                    .load(notificationsModel.getShared_user_profile_pic())
                    .placeholder(R.drawable.progress_icon)
                    .fitCenter()
                    .transform(new CircleTransform(context))
                    *//*.override(50, 50)*//*
                    .into(holder.notification_Image);*/
            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(context.getResources());
            builder.setProgressBarImage(R.drawable.loader);
            mResizeOptions = new ResizeOptions(50, 50);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .setRoundingParams(roundingParams)
                    .build();

            holder.notification_Image.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(notificationsModel.getShared_user_profile_pic()))
                            .setResizeOptions(mResizeOptions)

                            .build();
            holder.notification_Image.setImageRequest(imageRequest);


        } else {
          /*  Glide.with(context)
                    .load(R.drawable.default_user_image)
                    .centerCrop()
                    .transform(new CircleTransform(context))
                    *//*.override(50, 50)*//*
                    .into(holder.notification_Image);*/

            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.default_user_image))

                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(mResizeOptions)

                            .build();
            holder.notification_Image.setImageRequest(imageRequest2);
        }

        notificationsSetDetail = notificationsModel.getNotificationsSetDetail();

        holder.notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((notificationsModel.getType().equals("set")) && notificationsModel.getAction().equals("deleted")) {

                    Toast.makeText(context, "This Set is Deleted...", Toast.LENGTH_SHORT).show();

                } else if ((notificationsModel.getType().equals("card")) && notificationsModel.getAction().equals("deleted")) {

                    Toast.makeText(context, "This Card is Deleted...", Toast.LENGTH_SHORT).show();

                } else if (notificationsModel.getAction().equals("revoked")) {
                    Toast.makeText(context, notificationsModel.getNotificationsSetDetail().getName() + " set permission has been Revoked.", Toast.LENGTH_SHORT).show();
                } else if (notificationsModel.getAction().equals("comment")) {
                    Fragment fragment = new CommentsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putParcelable("notfy_modl_obj", notificationsModel);

                    fragment.setArguments(bundle);
                    mListener.onMessagePass(fragment);
                } else {
                    /*Intent intent = new Intent(context, CardDetailFragment.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("notfy_modl_obj", notificationsModel);
                    intent.putExtra("isNotification", true);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                    Fragment fragment = new CardDetailFragment();
                    Bundle bundle = new Bundle();
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
        SimpleDraweeView notification_Image;
        RelativeLayout notification_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            notification_Image = itemView.findViewById(R.id.notification_Image);
            notification_content = itemView.findViewById(R.id.notification_content);
            notification_dateTime = itemView.findViewById(R.id.notification_dateTime);
            notification_layout = itemView.findViewById(R.id.notification_layout);
        }
    }

    public interface MessagePassInterface {
        public void onMessagePass(Fragment fragment);
    }
}
