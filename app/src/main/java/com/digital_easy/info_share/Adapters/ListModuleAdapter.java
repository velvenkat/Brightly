package com.digital_easy.info_share.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digital_easy.info_share.Modules.AdminSettingsModel;
import com.digital_easy.info_share.Modules.AdminSettingsModuleData;
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

public class ListModuleAdapter extends RecyclerView.Adapter<ListModuleAdapter.ViewHolder> {

    Context scrn_context;
    List<AdminSettingsModuleData> adminSettingsModelList;

    public ListModuleAdapter(Context contxt, List<AdminSettingsModuleData> model_obj) {
        adminSettingsModelList = model_obj;
        scrn_context = contxt;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(scrn_context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout._lo_view_module, parent, false);
        // Return a new holder instance
        return new ListModuleAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminSettingsModuleData data_obj = adminSettingsModelList.get(position);

        ResizeOptions mResizeOptions = new ResizeOptions(120, 120);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(scrn_context.getResources());
        builder.setProgressBarImage(R.drawable.loader);

        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        holder.img_module.setHierarchy(hierarchy);


        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(data_obj.getWebIcon()))
                        .setResizeOptions(mResizeOptions)

                        .build();
        holder.img_module.setImageRequest(imageRequest);

        holder.txt_Module_name.setText(data_obj.getLabelName());

    }

    @Override
    public int getItemCount() {
        return adminSettingsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_Module_name;
        SimpleDraweeView img_module;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_Module_name = itemView.findViewById(R.id.txt_module);
            img_module = itemView.findViewById(R.id.img_module);

        }
    }
}
