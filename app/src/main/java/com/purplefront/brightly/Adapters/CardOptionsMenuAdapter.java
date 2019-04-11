package com.purplefront.brightly.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.purplefront.brightly.Modules.OptionsModel;
import com.purplefront.brightly.R;

import java.util.ArrayList;

public class CardOptionsMenuAdapter extends RecyclerView.Adapter<CardOptionsMenuAdapter.MyViewHolder> {
    Context scrn_contxt;
    public ArrayList<OptionsModel> models_list;
    OptsMenuInterface mListener;

    public CardOptionsMenuAdapter(Context context, OptsMenuInterface listener) {
        scrn_contxt = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(scrn_contxt);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.card_opts_mnu_item, parent, false);
        // Return a new holder instance
        return new CardOptionsMenuAdapter.MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtOptions.setText(models_list.get(position).opts_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOptsMenuSelected(models_list.get(position).Opts_Type);
            }
        });
        /*Glide.with(scrn_contxt)
                .load(models_list.get(position).img_url)
                .asBitmap()
                .placeholder(R.drawable.progress_icon)

//                    .transform(new CircleTransform(scrn_context))
                *//*.override(50, 50)*//*
                .into(holder.imageOptions);*/
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(scrn_contxt.getResources());
        builder.setProgressBarImage(R.drawable.loader);
        ResizeOptions mResizeOptions = new ResizeOptions(50, 50);
        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        holder.imageOptions.setHierarchy(hierarchy);


        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(models_list.get(position).img_url))
                        .setResizeOptions(mResizeOptions)

                        .build();
        holder.imageOptions.setImageRequest(imageRequest);

    }

    @Override
    public int getItemCount() {
        return models_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imageOptions;
        TextView txtOptions;


        public MyViewHolder(View itemView) {
            super(itemView);
            imageOptions = itemView.findViewById(R.id.imgOptions);
            txtOptions = itemView.findViewById(R.id.txtOptions);
        }
    }

    public interface OptsMenuInterface {
        public void onOptsMenuSelected(int Type);
    }
}
