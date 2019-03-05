package com.purplefront.brightly.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.purplefront.brightly.Modules.OptionsModel;
import com.purplefront.brightly.R;

import java.util.ArrayList;

public class CardOptionsMenuAdapter extends RecyclerView.Adapter<CardOptionsMenuAdapter.MyViewHolder> {
    Context scrn_contxt;
    public ArrayList<OptionsModel> models_list;
    OptsMenuInterface mListener;

    public CardOptionsMenuAdapter(Context context,OptsMenuInterface listener) {
        scrn_contxt = context;
        mListener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(scrn_contxt);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.card_opts_mnu_item, parent, false);
        // Return a new holder instance
        return new CardOptionsMenuAdapter.MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtOptions.setText(models_list.get(position).opts_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mListener.onOptsMenuSelected(models_list.get(position).Opts_Type);
            }
        });
        Glide.with(scrn_contxt)
                .load(models_list.get(position).img_url)
                .asBitmap()
                .placeholder(R.drawable.progress_icon)

//                    .transform(new CircleTransform(scrn_context))
                /*.override(50, 50)*/
                .into(holder.imageOptions);
    }

    @Override
    public int getItemCount() {
        return models_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOptions;
        TextView txtOptions;


        public MyViewHolder(View itemView) {
            super(itemView);
            imageOptions = itemView.findViewById(R.id.imgOptions);
            txtOptions = itemView.findViewById(R.id.txtOptions);
        }
    }
   public interface OptsMenuInterface{
        public void onOptsMenuSelected(int Type);
   }
}
