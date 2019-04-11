package com.purplefront.brightly.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purplefront.brightly.Modules.CommentsModel;
import com.purplefront.brightly.R;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    List<CommentsModel> commentsModelListObj;
    Context scrn_contxt;
    Chat_Sel_interface mListener;

    public ChatListAdapter(Context context, List<CommentsModel> commentsModelList, Chat_Sel_interface listener) {

        scrn_contxt = context;
        commentsModelListObj = commentsModelList;
        mListener = listener;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {

        CommentsModel model_obj = commentsModelListObj.get(position);
        holder.text_Comments.setText(model_obj.comment_mode);
        holder.text_CommentBy.setText(model_obj.created_name);
        holder.text_CrtdDays.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelect(position, model_obj);
            }
        });
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

    public interface Chat_Sel_interface {
        public void onSelect(int position, CommentsModel modelObj);


        //  public void onUnSelect(int position, SetsListModel modelObj);
    }
}
