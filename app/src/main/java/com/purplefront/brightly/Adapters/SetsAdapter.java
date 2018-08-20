package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.Activities.MyChannelsSet;
import com.purplefront.brightly.Activities.MySetCards;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.ViewHolder>{

    List<SetsListModel> setsListModels;
    Activity context;
    LayoutInflater inflater;
    Realm realm;
    RealmResults<RealmModel> realmModel;
    String userId;
    String channel_id;


    public SetsAdapter(MyChannelsSet myChannelsSet, List<SetsListModel> setsListModels, String channel_id) {

        this.context = myChannelsSet;
        this.setsListModels = setsListModels;
        this.channel_id = channel_id;
        inflater = (LayoutInflater.from(context));
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
    public void onBindViewHolder(@NonNull SetsAdapter.ViewHolder holder, int position) {

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for(RealmModel model:realmModel){
            userId = model.getUser_Id();
        }

        SetsListModel setsListModel = setsListModels.get(position);
        holder.textView_setName.setText(setsListModel.getSet_name());

        if(!setsListModel.getThumbnail().isEmpty()) {

            Glide.with(context)
                    .load(setsListModel.getThumbnail())
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(holder.imageView_setImage);
        }
        else
        {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(holder.imageView_setImage);
        }

        holder.channel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, MySetCards.class);
                intent.putExtra("userId", userId);
                intent.putExtra("set_id", setsListModel.getSet_id());
                intent.putExtra("set_name", setsListModel.getSet_name());
                intent.putExtra("set_description", setsListModel.getDescription());
                intent.putExtra("channel_id", channel_id);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

    }

    @Override
    public int getItemCount() {
        return setsListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_setName;
        ImageView imageView_setImage;
        CardView channel_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            channel_layout = itemView.findViewById(R.id.set_layout);
            imageView_setImage = itemView.findViewById(R.id.imageView_setImage);
            textView_setName = itemView.findViewById(R.id.textView_setName);
        }
    }
}
