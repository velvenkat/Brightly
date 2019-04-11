package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Fragments.SetsFragment;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.R;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyChannelsAdapter extends RecyclerView.Adapter<MyChannelsAdapter.ViewHolder> {

    List<ChannelListModel> channelListModels;
    Context scrn_context;
    LayoutInflater inflater;
    Realm realm;
    RealmResults<RealmModel> realmModel;
    String userId;
    String Created_by;
    int ScreenWidth_x;
    ChannelListItemClickListener mListener;
    public String chl_def_img_url;


    public MyChannelsAdapter(Context context, List<ChannelListModel> channelListModels, ChannelListItemClickListener listener, int ScreenWidth) {

        this.scrn_context = context.getApplicationContext();
        this.channelListModels = channelListModels;
        ScreenWidth_x = ScreenWidth;
        mListener = listener;

    }

    @NonNull
    @Override
    public MyChannelsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //  Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(scrn_context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.items_channel_lists, parent, false);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChannelsAdapter.ViewHolder holder, int position) {

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        /*ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenWidth_x / 2);
        holder.itemView.setLayoutParams(params);*/
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenWidth_x / 2) - 90);
        holder.imageView_channelImage.setLayoutParams(params2);
        ResizeOptions mResizeOptions = new ResizeOptions(300, 250);
        for (RealmModel model : realmModel) {
            userId = model.getUser_Id();
        }


        ChannelListModel channelListModel = channelListModels.get(position);
        String count = channelListModel.getTotal_set_count();
        holder.textView_channelName.setText(channelListModel.getChannel_name());
        holder.textView_channelCount.setText(count);
        Created_by = channelListModel.getCreated_by();

        if (!Created_by.equals(userId)) {
            holder.subscribed_icon.setVisibility(View.VISIBLE);
        } else {
            holder.subscribed_icon.setVisibility(View.GONE);
        }

        String img_url;

        if (channelListModel.getCover_image() != null && !channelListModel.getCover_image().trim().equals("")) {
            img_url = channelListModel.getCover_image();
        } else {
            img_url = chl_def_img_url;
        }

        /*    Glide.with(scrn_context)
                    .load(channelListModel.getCover_image())
                    .asBitmap()
                    .placeholder(R.drawable.progress_icon)

//                    .transform(new CircleTransform(scrn_context))
                    *//*.override(50, 50)*//*
                    .into(holder.imageView_channelImage);*/
            /*Glide.with(scrn_context)
                    .load(chl_def_img_url)
                    .asBitmap()
                    .fitCenter()
//                    .transform(new CircleTransform(scrn_context))
                    *//*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*//*
                    .into(holder.imageView_channelImage);*/
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(scrn_context.getResources());
        builder.setProgressBarImage(R.drawable.loader);

        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        holder.imageView_channelImage.setHierarchy(hierarchy);


        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(img_url))
                        .setResizeOptions(mResizeOptions)

                        .build();
        holder.imageView_channelImage.setImageRequest(imageRequest);


        holder.channel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*    Intent intent = new Intent(scrn_context, MyChannelsSet.class);
                 *//*  intent.putExtra("channel_id", channelListModel.getChannel_id());
                intent.putExtra("channel_name", channelListModel.getChannel_name());
                intent.putExtra("channel_description", channelListModel.getDescription());
                intent.putExtra("encoded_string", channelListModel.getCover_image());
                intent.putExtra("image_name", channelListModel.getImage_name());*//*
                intent.putExtra("model_obj",channelListModel);
                scrn_context.startActivity(intent);
                scrn_context.overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                Fragment fragment = new SetsFragment();
              /*  Bundle bundle = new Bundle();
                bundle.putParcelable("model_obj", channelListModel);*/
                //  fragment.setArguments(bundle);
                mListener.OnChannelItemClick(fragment, channelListModel);
                //  scrn_context.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

    }

    @Override
    public int getItemCount() {
        return channelListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_channelName;
        SimpleDraweeView imageView_channelImage;
        CardView channel_layout;
        ImageView subscribed_icon;
        TextView textView_channelCount;

        public ViewHolder(View itemView) {
            super(itemView);
            channel_layout = itemView.findViewById(R.id.channel_layout);
            //  channel_layout.setBackgroundResource(R.drawable.folder_catg);
            imageView_channelImage = itemView.findViewById(R.id.imageView_channelImage);
            subscribed_icon = itemView.findViewById(R.id.shared_icon);
            textView_channelName = itemView.findViewById(R.id.textView_channelName);
            textView_channelCount = itemView.findViewById(R.id.textView_channelCount);
        }
    }

    public interface ChannelListItemClickListener {
        public void OnChannelItemClick(Fragment call_frag, ChannelListModel chl_list_obj);
    }
}
