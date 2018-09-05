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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.ViewHolder> {

    List<SetsListModel> setsListModels;
    Activity context;
    LayoutInflater inflater;
    Realm realm;
    RealmResults<RealmModel> realmModel;
    String userId;
    String channel_id;
    String share_link;
    Set_sel_interface mListener;
    boolean isSelToDel = false;


    public SetsAdapter(MyChannelsSet myChannelsSet, List<SetsListModel> setsListModels, String channel_id, Set_sel_interface listenr) {

        this.context = myChannelsSet;
        this.setsListModels = setsListModels;
        this.channel_id = channel_id;
        inflater = (LayoutInflater.from(context));
        mListener = listenr;
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
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull SetsAdapter.ViewHolder holder, int position) {

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for (RealmModel model : realmModel) {
            userId = model.getUser_Id();
        }

        SetsListModel setsListModel = setsListModels.get(position);
        holder.textView_setName.setText(setsListModel.getSet_name());
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
        if (!setsListModel.getThumbnail().isEmpty()) {

            Glide.with(context)
                    .load(setsListModel.getThumbnail())
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(holder.imageView_setImage);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(holder.imageView_setImage);
        }
        holder.chkbx_del_set.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

                }
            }
        });
        holder.channel_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isSelToDel = true;
                mListener.onSelect(position, setsListModel);
                return true;
            }
        });

        holder.channel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();

                if (isSelToDel) {
                    mListener.onSelect(position, setsListModel);
                } else {

                    share_link = setsListModel.getShare_link();
                    Intent intent = new Intent(context, MySetCards.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("set_id", setsListModel.getSet_id());
                    intent.putExtra("set_name", setsListModel.getSet_name());
                    intent.putExtra("set_description", setsListModel.getDescription());
                    intent.putExtra("channel_id", channel_id);
                    intent.putExtra("share_link", share_link);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
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
        ImageView imageView_setImage;
        CardView channel_layout;
        CheckBox chkbx_del_set;

        public ViewHolder(View itemView) {
            super(itemView);
            channel_layout = itemView.findViewById(R.id.set_layout);
            imageView_setImage = itemView.findViewById(R.id.imageView_setImage);
            textView_setName = itemView.findViewById(R.id.textView_setName);
            chkbx_del_set = itemView.findViewById(R.id.chk_set_sel);
        }
    }

    public interface Set_sel_interface {
        public void onSelect(int position, SetsListModel modelObj);
      //  public void onUnSelect(int position, SetsListModel modelObj);
    }
}
