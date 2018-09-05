package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.Activities.CardList;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder>{

    private ProgressDialog dialog;
    String image_name;
    String image_id;

    List<CardsListModel> cardsListModels;
    Activity context;
    LayoutInflater inflater;
    Card_sel_interface mListener;

    boolean isSelToDel=false;

    public CardListAdapter(CardList cardList, List<CardsListModel> cardsListModels,Card_sel_interface listener) {

        this.context = cardList;
        this.cardsListModels = cardsListModels;
        inflater = (LayoutInflater.from(context));
        mListener=listener;
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
    @Override
    public int getItemViewType(int position) {
        if(isSelToDel)
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


        if (!cardsListModel.getUrl().isEmpty()) {

            dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Glide.with(context)
                    .load(cardsListModel.getUrl())
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(holder.image_cardImage);
            dialog.cancel();
            dialog.dismiss();

        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(holder.image_cardImage);
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

//                Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();

                if (isSelToDel) {
                    mListener.onSelect(position, cardsListModel);
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

        public ViewHolder(View itemView) {
            super(itemView);

            image_cardImage = itemView.findViewById(R.id.image_cardImage);
            text_cardName = itemView.findViewById(R.id.text_cardName);
            text_cardDescription = itemView.findViewById(R.id.text_cardDescription);
            chkbx_del_set=itemView.findViewById(R.id.chk_card_sel);
        }
    }

    public interface Card_sel_interface {
        public void onSelect(int position, CardsListModel modelObj);
        //  public void onUnSelect(int position, SetsListModel modelObj);
    }
}
