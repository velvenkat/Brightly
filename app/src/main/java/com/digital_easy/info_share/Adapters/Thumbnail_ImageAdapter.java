package com.digital_easy.info_share.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.digital_easy.info_share.Modules.MultipleImageModel;
import com.digital_easy.info_share.R;

import java.util.List;

public class Thumbnail_ImageAdapter extends RecyclerView.Adapter<Thumbnail_ImageAdapter.ThumbnailViewHolder> {

    Context scrn_contxt;
    public List<MultipleImageModel> multipleImageModelList;
    public Thumbnail_interface mListener;
    public int adapter_Thumbnail_sel_position = 1;
    public int adapter_multi_View_sel_pos;


    @NonNull
    @Override
    public ThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        scrn_contxt = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(scrn_contxt);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.temp_thumb_img, parent, false);
        // Return a new holder instance
        return new ThumbnailViewHolder(contactView);

    }

    public void setImage() {

    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, int position) {
        MultipleImageModel model_obj = multipleImageModelList.get(position);
        ResizeOptions mResizeOptions = new ResizeOptions(100, 100);
        if (adapter_Thumbnail_sel_position == position) {
            holder.rl_thumbnail_contr.setBackground(scrn_contxt.getResources().getDrawable(R.drawable.thumbnail_sel_border));
        } else {
            holder.rl_thumbnail_contr.setBackground(scrn_contxt.getResources().getDrawable(R.drawable.thumbnail_unsel_border));
        }
        /*else
            holder.rl_thumbnail_contr.setBackground(scrn_contxt.getResources().getColor(Color.TRANSPARENT));*/
        if (position == 0) {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.add_image_white))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(mResizeOptions)
                            .build();
            holder.img_thumbnial.setImageRequest(imageRequest2);

        } else {

            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);

            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(scrn_contxt.getResources());
            builder.setProgressBarImage(R.drawable.loader);

            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            holder.img_thumbnial.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(model_obj.getImg_url()))
                            .setResizeOptions(mResizeOptions)

                            .build();
            holder.img_thumbnial.setImageRequest(imageRequest);

        }
        holder.img_thumbnial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {

                    mListener.Add_Thumbnail();
                } else {
                    mListener.onSelect(position, model_obj);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return multipleImageModelList.size();
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img_thumbnial;
        RelativeLayout rl_thumbnail_contr;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            img_thumbnial = itemView.findViewById(R.id.img_thumb);
            rl_thumbnail_contr = itemView.findViewById(R.id.rl_thumbnail_contr);
        }
    }

    public interface Thumbnail_interface {
        public void Add_Thumbnail();

        public void onSelect(int Position, MultipleImageModel model);
    }
}
