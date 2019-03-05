package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.purplefront.brightly.Fragments.CardList;
import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CommentsModel;
import com.purplefront.brightly.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    List<CommentsModel> commentsModelListObj;
    Context scrn_contxt;
    Comments_Sel_interface mListener;

    public CommentsAdapter(Context context, List<CommentsModel> commentsModelList, Comments_Sel_interface listener) {

        scrn_contxt = context;
        commentsModelListObj = commentsModelList;
        listener = mListener;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_commtents, parent, false);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {

        CommentsModel model_obj = commentsModelListObj.get(position);
        holder.text_Comments.setText(model_obj.comment);
        holder.text_CommentBy.setText(model_obj.created_name);
        holder.text_CrtdDays.setText(model_obj.created_time);
    }


    @Override
    public int getItemCount() {
        return commentsModelListObj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_Comments;

        TextView text_CommentBy;
        TextView text_CrtdDays;

        public ViewHolder(View itemView) {
            super(itemView);

            text_Comments = itemView.findViewById(R.id.text_comment);
            text_CommentBy = itemView.findViewById(R.id.text_comment_by);
            text_CrtdDays = itemView.findViewById(R.id.txt_comtd_date);

        }
    }

    public interface Comments_Sel_interface {
        public void onSelect(int position, CardsListModel modelObj);


        //  public void onUnSelect(int position, SetsListModel modelObj);
    }
}
