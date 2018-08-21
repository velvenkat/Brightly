package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.Activities.CardList;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.R;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder>{

    private ProgressDialog dialog;
    String image_name;
    String image_id;

    List<CardsListModel> cardsListModels;
    Activity context;
    LayoutInflater inflater;


    public CardListAdapter(CardList cardList, List<CardsListModel> cardsListModels) {

        this.context = cardList;
        this.cardsListModels = cardsListModels;
        inflater = (LayoutInflater.from(context));
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

    @Override
    public void onBindViewHolder(@NonNull CardListAdapter.ViewHolder holder, int position) {

        CardsListModel cardsListModel = cardsListModels.get(position);

        if (cardsListModel.getTitle() != null) {
            holder.text_cardName.setText(cardsListModel.getTitle());
        }

        if (cardsListModel.getDescription() != null) {
            holder.text_cardDescription.setText(cardsListModel.getDescription());
        }

        image_id = cardsListModel.getImage_id();
        image_name = cardsListModel.getImage_name();

        if (!cardsListModel.getImgUrl().isEmpty()) {

            dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Glide.with(context)
                    .load(cardsListModel.getImgUrl())
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


    }

    @Override
    public int getItemCount() {
        return cardsListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_cardName;
        TextView text_cardDescription;
        ImageView image_cardImage;

        public ViewHolder(View itemView) {
            super(itemView);

            image_cardImage = itemView.findViewById(R.id.image_cardImage);
            text_cardName = itemView.findViewById(R.id.text_cardName);
            text_cardDescription = itemView.findViewById(R.id.text_cardDescription);
        }
    }
}
