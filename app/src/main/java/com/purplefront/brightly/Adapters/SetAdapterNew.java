package com.purplefront.brightly.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.purplefront.brightly.Activities.BrightlyNavigationActivity;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;

import java.util.List;

public class SetAdapterNew extends RecyclerView.Adapter<SetAdapterNew.ViewHolder> {


    //    private ProgressDialog dialog;
    String image_name;
    String image_id;

    List<CardsListModel> cardsListModelsList;
    Context scrn_context;
    CardsListModel cardsListModelObj = null;
    SetsListModel setsListModelObj = null;

    Set_sel_interface mListener;
    public String set_default_img;
    boolean isSelToDel = false;
    public boolean isCardHighlight = false;
    public int SelectedPos = -1;
    public List<SetsListModel> setsListModelsList;
    public boolean isCopyCard = false;

    public SetAdapterNew(Context context, List<SetsListModel> cardsListModels, Set_sel_interface listener) {

        this.scrn_context = context;
        this.setsListModelsList = cardsListModels;
        mListener = listener;
    }

    @NonNull
    @Override
    public SetAdapterNew.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.items_card_lists, parent, false);
        // Return a new holder instance
        return new SetAdapterNew.ViewHolder(contactView);
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
    public void onBindViewHolder(@NonNull SetAdapterNew.ViewHolder holder, int position) {


        if (!isSelToDel) {
            if (isCopyCard) {
                holder.img_set_menu.setVisibility(View.INVISIBLE);
            } else
                holder.img_set_menu.setVisibility(View.VISIBLE);
        } else
            holder.chkbx_del_set.setVisibility(View.VISIBLE);

        setsListModelObj = setsListModelsList.get(position);
        holder.text_cardName.setText(setsListModelObj.getSet_name());
        holder.text_cardDescription.setText(setsListModelObj.getDescription());
        ResizeOptions mResizeOptions = new ResizeOptions(100, 100);
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        String set_img_url;
        if (setsListModelObj.getThumbnail().trim().equals(""))
            set_img_url = set_default_img;
        else {
            set_img_url = setsListModelObj.getThumbnail();
        }
        //String set_default_img = ((BrightlyNavigationActivity)scrn_context).SET_DEF_IMAGE;
           /* Glide.with(scrn_context)
                    .load(set_default_img)

                    .fitCenter()
                    *//*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*//*
                    .into(holder.image_cardImage);*/

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(scrn_context.getResources());
        builder.setProgressBarImage(R.drawable.loader);
        builder.setRoundingParams(roundingParams);
        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        holder.image_cardImage.setHierarchy(hierarchy);


        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(set_img_url))
                        .setResizeOptions(mResizeOptions)

                        .build();
        holder.image_cardImage.setImageRequest(imageRequest);





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

                mListener.onCardClick(position);

            }
        });
        holder.img_set_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //creating a popup menu
                PopupMenu popup = new PopupMenu(scrn_context, holder.img_set_menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_info:
                                //handle menu1 click
                                mListener.onSelect(position, v, false);
                                return true;
                            case R.id.menu_share:
                                //handle menu2 click
                                mListener.onSelect(position, v, true);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

    }


    @Override
    public int getItemCount() {

        return setsListModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_cardName;
        TextView text_cardDescription;
        SimpleDraweeView image_cardImage;
        CheckBox chkbx_del_set;
        CardView cardView;
        ImageView img_set_menu;

        public ViewHolder(View itemView) {
            super(itemView);

            image_cardImage = itemView.findViewById(R.id.image_cardImage);
            text_cardName = itemView.findViewById(R.id.text_cardName);
            text_cardDescription = itemView.findViewById(R.id.text_cardDescription);
            chkbx_del_set = itemView.findViewById(R.id.chk_card_sel);
            cardView = itemView.findViewById(R.id.cardVw_card_list);
            img_set_menu = itemView.findViewById(R.id.img_set_menu);
        }
    }

    public interface Set_sel_interface {
        public void onSelect(int position, View view, boolean isShare);


        public void onCardClick(int position);
        //  public void onUnSelect(int position, SetsListModel modelObj);
    }


}
