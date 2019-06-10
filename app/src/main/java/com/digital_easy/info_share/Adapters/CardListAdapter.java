package com.digital_easy.info_share.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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
import com.digital_easy.info_share.Modules.CardsListModel;
import com.digital_easy.info_share.Modules.MultipleImageModel;
import com.digital_easy.info_share.R;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    String image_name;
    String image_id;
    public String audio_def_image;
    public String file_def_image;
    public String uTube_def_image;
    public String def_image;
    List<CardsListModel> cardsListModels;
    Context scrn_context;

    Card_sel_interface mListener;

    boolean isSelToDel = false;
    public boolean isCardHighlight = false;
    public int SelectedPos = -1;


    public CardListAdapter(Context context, List<CardsListModel> cardsListModels, Card_sel_interface listener) {

        this.scrn_context = context;
        this.cardsListModels = cardsListModels;
        mListener = listener;
    }

    @NonNull
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.items_card_lists, parent, false);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    public void set_SelToDel(boolean value) {
        isSelToDel = value;

    }

    public boolean isSelToDel() {
        return isSelToDel;
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
    public void onBindViewHolder(@NonNull CardListAdapter.ViewHolder holder, int position) {

        CardsListModel cardsListModel = cardsListModels.get(position);
        if (SelectedPos == position) {
            if (isCardHighlight) {
                holder.itemView.setBackgroundColor(scrn_context.getResources().getColor(R.color.light_gery));
            }
        } else {
            holder.cardView.setBackgroundColor(scrn_context.getResources().getColor(R.color.white));
        }
        if (cardsListModel.getTitle() != null) {
            holder.text_cardName.setText(cardsListModel.getTitle());
        }
        if (cardsListModel.getType().equals("contact")) {
            holder.text_cardName.setText(cardsListModel.getContact_name());
        }
        if (cardsListModel.getDescription() != null) {
            holder.text_cardDescription.setText(cardsListModel.getDescription());
        }

        image_id = cardsListModel.getCard_id();
        image_name = cardsListModel.getName();

        if (isSelToDel) {
            holder.chkbx_del_set.setVisibility(View.VISIBLE);
        } else {
            holder.chkbx_del_set.setVisibility(View.INVISIBLE);
        }
        if (cardsListModel.isDelSel()) {
            holder.chkbx_del_set.setChecked(true);

        } else {
            holder.chkbx_del_set.setChecked(false);
        }

        String img_url;
        switch (cardsListModel.getType()) {

            case "multiple_images":
                try {
                    MultipleImageModel modelObj = cardsListModel.getMultiple_img_url().get(0);
                    img_url = modelObj.getImg_url();
                } catch (Exception e) {
                    img_url = def_image;
                }
                //
                /*Glide.with(scrn_context)
                        .load(cardsListModel.getUrl())
                        .placeholder(R.drawable.progress_icon)
                        .fitCenter()
                        *//*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*//*
                        .into(holder.image_cardImage);
*/
                break;
            case "contact":
                try {
                    MultipleImageModel modelObj = cardsListModel.getMultiple_img_url().get(0);
                    img_url = modelObj.getImg_url();
                } catch (Exception e) {
                    img_url = def_image;
                }
                break;
            case "audio":

                img_url = audio_def_image;

  /*              Glide.with(scrn_context)
                        .load(R.drawable.audio_icon)
                        .placeholder(R.drawable.progress_icon)
                        .fitCenter()
                        *//*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*//*
                        .into(holder.image_cardImage);
*/
                break;
            case "video":
                img_url = uTube_def_image;

//                holder.image_cardImage.setPadding(0,20,0,20);

  /*              Glide.with(scrn_context)
                        .load(R.drawable.youtube_icon)
                        .placeholder(R.drawable.progress_icon)
                        .fitCenter()
                        *//*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*//*
                        .into(holder.image_cardImage);
*/
                break;


            case "file":
                img_url = file_def_image;

                /*Glide.with(scrn_context)
                        .load(R.drawable.file_icon)
                        .placeholder(R.drawable.progress_icon)
                        .fitCenter()
                        *//*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*//*
                        .into(holder.image_cardImage);
*/
                break;
            default:
                img_url = def_image;
                /*Glide.with(scrn_context)
                        .load(R.drawable.image_icon)
                        .placeholder(R.drawable.progress_icon)
                        .centerCrop()
                        *//*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*//*
                        .into(holder.image_cardImage);*/
                break;
        }
        if (img_url != null && !img_url.equals("")) {
            ResizeOptions mResizeOptions = new ResizeOptions(100, 100);
            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setRoundAsCircle(true);

            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(scrn_context.getResources());
            builder.setProgressBarImage(R.drawable.loader);

            builder.setProgressBarImage(
                    new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
            builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(100)
                    .setRoundingParams(roundingParams)
                    .build();

            holder.image_cardImage.setHierarchy(hierarchy);


            final ImageRequest imageRequest =
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(img_url))
                            .setResizeOptions(mResizeOptions)

                            .build();
            holder.image_cardImage.setImageRequest(imageRequest);
        } else {
            /*Glide.with(scrn_context)
                    .load(R.drawable.image_icon)
                    .placeholder(R.drawable.progress_icon)
                    .centerCrop()
                    *//*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*//*
                    .into(holder.image_cardImage);*/
            ResizeOptions mResizeOptions = new ResizeOptions(50, 50);
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.image_icon))
                    .build();
            final ImageRequest imageRequest2 =
                    ImageRequestBuilder.newBuilderWithSource(uri)
                            .setResizeOptions(mResizeOptions)
                            .build();
            holder.image_cardImage.setImageRequest(imageRequest2);
        }
       /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isSelToDel = true;
                mListener.onSelect(position, cardsListModel);
                return true;
            }
        });*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(scrn_context, "Working", Toast.LENGTH_SHORT).show();

                if (isSelToDel) {
                    mListener.onSelect(position, cardsListModel);
                } else {
                    mListener.onCardClick(position);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return cardsListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_cardName;
        TextView text_cardDescription;
        SimpleDraweeView image_cardImage;
        CheckBox chkbx_del_set;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            image_cardImage = itemView.findViewById(R.id.image_cardImage);
            text_cardName = itemView.findViewById(R.id.text_cardName);
            text_cardDescription = itemView.findViewById(R.id.text_cardDescription);
            chkbx_del_set = itemView.findViewById(R.id.chk_card_sel);
            cardView = itemView.findViewById(R.id.cardVw_card_list);
        }
    }

    public interface Card_sel_interface {
        public void onSelect(int position, CardsListModel modelObj);

        public void onCardClick(int position);
        //  public void onUnSelect(int position, SetsListModel modelObj);
    }
}
