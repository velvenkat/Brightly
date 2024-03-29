package com.digital_easy.info_share.Adapters;

import android.app.Activity;
import android.net.Uri;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Modules.ChannelListModel;
import com.digital_easy.info_share.Modules.SetsListModel;
import com.digital_easy.info_share.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.ViewHolder> {

    ArrayList<SetsListModel> setsListModels;
    ChannelListModel chl_list_obj;
    Activity scrn_contxt;

    Realm realm;
    RealmResults<RealmModel> realmModel;

    Set_sel_interface mListener;
    boolean isSelToDel = false;
    String Created_By;
    boolean isCopyCard;


    public SetsAdapter(Activity context, ArrayList<SetsListModel> setsListModels, ChannelListModel chl_list_obj, Set_sel_interface listenr, boolean isCopy_Card) {


        this.scrn_contxt = context;
        this.setsListModels = setsListModels;
        this.chl_list_obj = chl_list_obj;
        mListener = listenr;
        isCopyCard = isCopy_Card;
    }


    public void set_SelToDel(boolean value) {
        isSelToDel = value;

    }

    @NonNull
    @Override
    public SetsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.items_sets_lists, parent, false);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public int getItemViewType(int position) {
        if (isSelToDel)
            return position;
        else
            return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull SetsAdapter.ViewHolder holder, int position) {

       /* realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for (RealmModel model : realmModel) {
            userId = model.getUser_Id();
        }*/


        SetsListModel setsListModel = setsListModels.get(position);
        if (setsListModel.getShare_access().equals("1")) {
            holder.img_share.setVisibility(View.VISIBLE);
        } else {
            holder.img_share.setVisibility(View.INVISIBLE);
        }
        if (isSelToDel) {
            holder.chkbx_del_set.setVisibility(View.VISIBLE);
        } else {
            holder.chkbx_del_set.setVisibility(View.INVISIBLE);
        }
        if (setsListModel.isDelSel()) {
            holder.chkbx_del_set.setChecked(true);

        } else {
            holder.chkbx_del_set.setChecked(false);
        }
        ResizeOptions mResizeOptions = new ResizeOptions(50, 50);
        if (!setsListModel.getThumbnail().isEmpty()) {

           /* Glide.with(scrn_contxt)
                    .load(setsListModel.getThumbnail())
                    .placeholder(R.drawable.progress_icon)
                    .fitCenter()
                    *//*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*//*
                    .into(holder.imageView_setImage);*/

            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(scrn_contxt.getResources());
            builder.setProgressBarImage(R.drawable.loader);
            mResizeOptions = new ResizeOptions(50, 50);
            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .build();

            holder.imageView_setImage.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(setsListModel.getThumbnail()))
                            .setResizeOptions(mResizeOptions)

                            .build();
            holder.imageView_setImage.setImageRequest(imageRequest);


        } else {
           /* Glide.with(scrn_contxt)
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    *//*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*//*
                    .into(holder.imageView_setImage);*/

            //   ResizeOptions mResizeOptions = new ResizeOptions(50, 50);
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.no_image_available))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(mResizeOptions)
                            .build();
            holder.imageView_setImage.setImageRequest(imageRequest2);

        }
        if (isCopyCard) {
            holder.img_share.setVisibility(View.GONE);
            holder.img_info.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
           /* LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,150);
            holder.set_contr.setLayoutParams(params);*/
        }
        String counts = setsListModel.getTotal_card_count();
        holder.textView_setName.setText(setsListModel.getSet_name());
        //    holder.textView_setCount.setText("("+counts+")");
        holder.chkbx_del_set.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {

                }
            }
        });

        /*holder.channel_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isSelToDel = true;
                mListener.onSelect(position, setsListModel);
                return true;
            }
        });
*/

        holder.rl_setItemContr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // return false;
                Bundle bundle = new Bundle();
                bundle.putParcelable("setsListModel", setsListModel);
                mListener.showPopUp(holder.itemView, bundle);
                return true;

            }
        });

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(scrn_contxt, "Share Clicked", Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putParcelable("setsListModel", setsListModel);
                mListener.onInfoShareClicked(true, bundle);
            }
        });
        holder.img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(scrn_contxt, "Info Clicked", Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putParcelable("setsListModel", setsListModel);
                mListener.onInfoShareClicked(false, bundle);
            }
        });
        holder.imageView_setImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("setsListModel", setsListModel);
                mListener.showPopUp(holder.itemView, bundle);
                return true;
            }
        });
        holder.imageView_setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(scrn_context, "Working", Toast.LENGTH_SHORT).show();

                if (isSelToDel) {
                    mListener.onSelect(position, setsListModel);
                } else {
                    /*Intent intent = new Intent(context, CardDetailFragment.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("model_obj", chl_list_obj);
                    intent.putExtra("setsListModel", setsListModel);
                    intent.putExtra("isNotification", false);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.right_enter, R.anim.left_out);*/
                    //Fragment card_dtl_frag=new CardDetailFragment();
                    Bundle bundle = new Bundle();
                    // bundle.putParcelable("model_obj", chl_list_obj);
                    bundle.putParcelable("setsListModel", setsListModel);
                    bundle.putBoolean("isNotification", false);
                    bundle.putString("chl_name", chl_list_obj.getChannel_name());
                    // card_dtl_frag.setArguments(bundle);
                    mListener.onCardShow(bundle, setsListModel.getTotal_card_count());
                    scrn_contxt.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return setsListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_setName;
        // TextView textView_setCount;
        SimpleDraweeView imageView_setImage;
        //  RelativeLayout channel_layout;
        CheckBox chkbx_del_set;
        ImageView img_info, img_share;
        RelativeLayout icon_contr, rl_setItemContr;
        View divider;
        //LinearLayout set_contr;

        public ViewHolder(View itemView) {
            super(itemView);
            //  channel_layout = itemView.findViewById(R.id.set_layout);
            imageView_setImage = itemView.findViewById(R.id.imageView_setImage);
            textView_setName = itemView.findViewById(R.id.textView_setName);
            rl_setItemContr = itemView.findViewById(R.id.rl_setItemContr);

            //  textView_setCount = itemView.findViewById(R.id.textView_setCount);
            chkbx_del_set = itemView.findViewById(R.id.chk_set_sel);
            img_info = itemView.findViewById(R.id.img_info);
            img_share = itemView.findViewById(R.id.img_share);
            icon_contr = itemView.findViewById(R.id.icon_contr);
            divider = itemView.findViewById(R.id.divider_1);
            //  set_contr=itemView.findViewById(R.id.set_contr);
        }
    }

    public interface Set_sel_interface {
        public void onSelect(int position, SetsListModel modelObj);

        public void onInfoShareClicked(boolean isShare, Bundle bundle);

        public void onCardShow(Bundle bundle_args, String card_count);

        public void showPopUp(View c, Bundle bundle_args);
    }
}
