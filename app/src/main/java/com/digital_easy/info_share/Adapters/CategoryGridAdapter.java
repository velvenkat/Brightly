package com.digital_easy.info_share.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Fragments.SetsFragment;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.R;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryGridAdapter extends BaseAdapter {
    Context scrn_contxt;
    List<ChannelListModel> model_list_obj;
    int ScreenWidth_x;
    String userId;
    String chl_def_img_url;
    ChannelListItemClickListener mListener;
    int Item_pos;

    public CategoryGridAdapter(Context context, List<ChannelListModel> model_list_obj_val, int item_pos, int ScreenWidth, String user_id, String chl_def_img_url_val, ChannelListItemClickListener listener) {
        scrn_contxt = context;
        ScreenWidth_x = ScreenWidth;
        chl_def_img_url = chl_def_img_url_val;
        mListener = listener;
        Item_pos = item_pos;
        model_list_obj = model_list_obj_val;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView_channelName;
        SimpleDraweeView imageView_channelImage;
        CardView channel_layout;
        ImageView subscribed_icon;
        TextView textView_channelCount;
        position += Item_pos;



        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(scrn_contxt);
            convertView = inflater.inflate(R.layout.items_channel_lists, null);


        }
        channel_layout = convertView.findViewById(R.id.channel_layout);

        //  channel_layout.setBackgroundResource(R.drawable.folder_catg);
        imageView_channelImage = convertView.findViewById(R.id.imageView_channelImage);
        subscribed_icon = convertView.findViewById(R.id.shared_icon);
        textView_channelName = convertView.findViewById(R.id.textView_channelName);
        textView_channelCount = convertView.findViewById(R.id.textView_channelCount);

        if (!(position  < model_list_obj.size())) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            ChannelListModel channel_model_obj = model_list_obj.get(position);
            textView_channelName.setText(channel_model_obj.getChannel_name());
            Realm realm = Realm.getDefaultInstance();
            RealmResults<RealmModel> realmModel;
            realmModel = realm.where(RealmModel.class).findAllAsync();
            realmModel.load();
        /*ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenWidth_x / 2);
        holder.itemView.setLayoutParams(params);*/
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenWidth_x / 2) - 90);
            imageView_channelImage.setLayoutParams(params2);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((ScreenWidth_x / 2), ViewGroup.LayoutParams.WRAP_CONTENT);
            channel_layout.setLayoutParams(params);


            ResizeOptions mResizeOptions = new ResizeOptions(300, 250);
            for (RealmModel model : realmModel) {
                userId = model.getUser_Id();
            }


            ChannelListModel channelListModel = model_list_obj.get(position);
            String count = channelListModel.getTotal_set_count();
            textView_channelName.setText(channelListModel.getChannel_name());
            textView_channelCount.setText(count);
            String Created_by = channelListModel.getCreated_by();

            if (!Created_by.equals(userId)) {
                subscribed_icon.setVisibility(View.VISIBLE);
            } else {
                subscribed_icon.setVisibility(View.GONE);
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
                    new GenericDraweeHierarchyBuilder(scrn_contxt.getResources());
            builder.setProgressBarImage(R.drawable.loader);

            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            imageView_channelImage.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img_url))
                            .setResizeOptions(mResizeOptions)

                            .build();
            imageView_channelImage.setImageRequest(imageRequest);


            channel_layout.setOnClickListener(new View.OnClickListener() {
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

        return convertView;
    }

    public interface ChannelListItemClickListener {
        public void OnChannelItemClick(Fragment call_frag, ChannelListModel chl_list_obj);
    }
}
