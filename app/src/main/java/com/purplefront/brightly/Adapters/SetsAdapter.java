package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

import com.purplefront.brightly.Fragments.CardDetailFragment;
import com.purplefront.brightly.Application.RealmModel;
import com.purplefront.brightly.Modules.ChannelListModel;
import com.purplefront.brightly.Modules.SetsListModel;
import com.purplefront.brightly.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.ViewHolder> {

    ArrayList<SetsListModel> setsListModels;
    ChannelListModel chl_list_obj;
    Context scrn_contxt;

    Realm realm;
    RealmResults<RealmModel> realmModel;
    String userId;
    String channel_id;
    String channel_name;
    String share_link;
    Set_sel_interface mListener;
    boolean isSelToDel = false;
    String Created_By;


    public SetsAdapter(Context context, ArrayList<SetsListModel> setsListModels,  ChannelListModel chl_list_obj, Set_sel_interface listenr) {


        this.scrn_contxt = context;
        this.setsListModels = setsListModels;
        this.chl_list_obj = chl_list_obj;
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
    public void onBindViewHolder(@NonNull SetsAdapter.ViewHolder holder, int position) {

        realm = Realm.getDefaultInstance();
        realmModel = realm.where(RealmModel.class).findAllAsync();
        realmModel.load();
        for (RealmModel model : realmModel) {
            userId = model.getUser_Id();
        }

        SetsListModel setsListModel = setsListModels.get(position);
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

            Glide.with(scrn_contxt)
                    .load(setsListModel.getThumbnail())
                    .fitCenter()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(holder.imageView_setImage);
        } else {
            Glide.with(scrn_contxt)
                    .load(R.drawable.no_image_available)
                    .centerCrop()
                    /*.transform(new CircleTransform(HomeActivity.this))
                    .override(50, 50)*/
                    .into(holder.imageView_setImage);
        }
        String counts = setsListModel.getTotal_card_count();
        holder.textView_setName.setText(setsListModel.getSet_name());
        holder.textView_setCount.setText("("+counts+")");
        holder.chkbx_del_set.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

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
        holder.channel_layout.setOnClickListener(new View.OnClickListener() {
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
                    Fragment card_dtl_frag=new CardDetailFragment();
                    Bundle bundle =new Bundle();
                    bundle.putParcelable("model_obj", chl_list_obj);
                    bundle.putParcelable("setsListModel", setsListModel);
                    bundle.putBoolean("isNotification", false);
                    card_dtl_frag.setArguments(bundle);
                    mListener.onCardShow(card_dtl_frag);
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
        TextView textView_setCount;
        ImageView imageView_setImage;
        CardView channel_layout;
        CheckBox chkbx_del_set;

        public ViewHolder(View itemView) {
            super(itemView);
            channel_layout = itemView.findViewById(R.id.set_layout);
            imageView_setImage = itemView.findViewById(R.id.imageView_setImage);
            textView_setName = itemView.findViewById(R.id.textView_setName);
            textView_setCount = itemView.findViewById(R.id.textView_setCount);
            chkbx_del_set = itemView.findViewById(R.id.chk_set_sel);
        }
    }

    public interface Set_sel_interface {
        public void onSelect(int position, SetsListModel modelObj);
        public void onCardShow(Fragment call_frag);
    }
}
