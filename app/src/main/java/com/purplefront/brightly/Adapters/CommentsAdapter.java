package com.purplefront.brightly.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.purplefront.brightly.Modules.CardsListModel;
import com.purplefront.brightly.Modules.CommentsModel;
import com.purplefront.brightly.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    List<CommentsModel> commentsModelListObj;
    Context scrn_contxt;
    Comments_Sel_interface mListener;
    String user_id;

    public CommentsAdapter(Context context, List<CommentsModel> commentsModelList, Comments_Sel_interface listener, String temp_usr_id) {

        scrn_contxt = context;
        commentsModelListObj = commentsModelList;
        listener = mListener;
        user_id = temp_usr_id;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.chat_layout, parent, false);
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
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {

        CommentsModel model_obj = commentsModelListObj.get(position);
        if (user_id.equals(model_obj.created_by)) {
            holder.own_comment.setText(model_obj.comment);
            holder.text_own_CrtdTime.setText(model_obj.created_time);
            holder.othr_comt_contr.setVisibility(View.GONE);
        } else {
            holder.other_comment.setText(model_obj.comment);
            holder.other_comtd_by.setText(model_obj.created_name);
            holder.text_other_crtd_time.setText(model_obj.created_time);
            holder.own_comt_contr.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return commentsModelListObj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView own_comment;

        TextView other_comment;
        TextView text_own_CrtdTime;
        TextView text_other_crtd_time;
        RelativeLayout othr_comt_contr, own_comt_contr;
        TextView other_comtd_by;

        public ViewHolder(View itemView) {
            super(itemView);

            own_comment = itemView.findViewById(R.id.own_comment);
            other_comment = itemView.findViewById(R.id.other_comt);
            text_own_CrtdTime = itemView.findViewById(R.id.own_crtd_time);
            text_other_crtd_time = itemView.findViewById(R.id.other_crtd_time);
            othr_comt_contr = itemView.findViewById(R.id.other_comt_contr);
            own_comt_contr = itemView.findViewById(R.id.own_comnt_contr);
            other_comtd_by = itemView.findViewById(R.id.other_comtd_by);


        }
    }

    public interface Comments_Sel_interface {
        public void onSelect(int position, CardsListModel modelObj);


        //  public void onUnSelect(int position, SetsListModel modelObj);
    }
}
