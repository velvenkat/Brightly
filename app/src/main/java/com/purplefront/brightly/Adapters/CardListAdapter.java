package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.Fragments.CardList;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.R;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private ProgressDialog dialog;
    String image_name;
    String image_id;

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
    public boolean isSelToDel()
    {
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
        if(SelectedPos==position){
            if(isCardHighlight){
                holder.itemView.setBackgroundColor(scrn_context.getResources().getColor(R.color.light_gery));
            }
        }
        else
        {
            holder.cardView.setBackgroundColor(scrn_context.getResources().getColor(R.color.white));
        }
        if (cardsListModel.getTitle() != null) {
            holder.text_cardName.setText(cardsListModel.getTitle());
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


        switch (cardsListModel.getType()) {
            case "image":

                dialog = new ProgressDialog(scrn_context);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Glide.with(scrn_context)
                        .load(cardsListModel.getUrl())
                        .fitCenter()
                        /*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*/
                        .into(holder.image_cardImage);
                dialog.cancel();
                dialog.dismiss();

                break;
            case "audio":

                holder.image_cardImage.setPadding(0, 20, 0, 20);

                Glide.with(scrn_context)
                        .load(R.drawable.audio_list_icon)
                        .fitCenter()
                        /*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*/
                        .into(holder.image_cardImage);

                break;
            case "video":

//                holder.image_cardImage.setPadding(0,20,0,20);

                Glide.with(scrn_context)
                        .load(R.drawable.youtube_list_icon)
                        .fitCenter()
                        /*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*/
                        .into(holder.image_cardImage);

                break;
            case "file":

                holder.image_cardImage.setPadding(0, 20, 0, 20);

                Glide.with(scrn_context)
                        .load(R.drawable.file_list_icon)
                        .fitCenter()
                        /*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*/
                        .into(holder.image_cardImage);

                break;
            default:
                Glide.with(scrn_context)
                        .load(R.drawable.no_image_available)
                        .centerCrop()
                        /*.transform(new CircleTransform(HomeActivity.this))
                        .override(50, 50)*/
                        .into(holder.image_cardImage);
                break;
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
        ImageView image_cardImage;
        CheckBox chkbx_del_set;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            image_cardImage = itemView.findViewById(R.id.image_cardImage);
            text_cardName = itemView.findViewById(R.id.text_cardName);
            text_cardDescription = itemView.findViewById(R.id.text_cardDescription);
            chkbx_del_set = itemView.findViewById(R.id.chk_card_sel);
            cardView=itemView.findViewById(R.id.cardVw_card_list);
        }
    }

    public interface Card_sel_interface {
        public void onSelect(int position, CardsListModel modelObj);

        public void onCardClick(int position);
        //  public void onUnSelect(int position, SetsListModel modelObj);
    }
}
