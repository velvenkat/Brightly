package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purplefront.brightly.Fragments.EditSetInfo;
import com.purplefront.brightly.Modules.SharedDataModel;
import com.purplefront.brightly.R;

import java.util.ArrayList;

public class SharedListAdapter extends RecyclerView.Adapter<SharedListAdapter.ViewHolder> {
    ArrayList<SharedDataModel> sharedDataModel;
    Context scrn_context;
    LayoutInflater inflater;
    String set_id;
    String share_link;
    SharedListInterface mListener;


    public SharedListAdapter(Context editSetInfo, ArrayList<SharedDataModel> sharedDataModel, String set_id, String share_link, SharedListInterface listener) {

        this.scrn_context = editSetInfo;
        this.sharedDataModel = sharedDataModel;
        this.set_id = set_id;
        this.share_link = share_link;
        inflater = (LayoutInflater.from(scrn_context));
        mListener=listener;
    }

    @NonNull
    @Override
    public SharedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.items_shared_lists, parent, false);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedListAdapter.ViewHolder holder, int position) {

        SharedDataModel sharedDataModels = sharedDataModel.get(position);

        if (sharedDataModels.getName() != null) {
            holder.text_sharedName.setText(sharedDataModels.getName());
        }
        if (sharedDataModels.getPhone() != null) {
            holder.text_sharedNumber.setText(sharedDataModels.getPhone());
        }


        if(sharedDataModels.getInstlled().equals("0"))
        {
            holder.inactive_user.setVisibility(View.VISIBLE);
            holder.active_user.setVisibility(View.GONE);

            holder.inactive_layout.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.inactive_user.setVisibility(View.GONE);
            holder.active_user.setVisibility(View.VISIBLE);
            holder.inactive_layout.setVisibility(View.GONE);
        }

        holder.app_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, scrn_context.getResources().getString(R.string.share_msg_subject));
                String url = "https://play.google.com/store/apps/details?id=purplefront.com.kriddrpetparent";
                share.putExtra(Intent.EXTRA_TEXT, scrn_context.getResources().getString(R.string.share_msg_text) + "\n " + url);
                scrn_context.startActivity(Intent.createChooser(share, "Share link!"));

            }
        });

        holder.web_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Brightly Set Share link");
                share.putExtra(Intent.EXTRA_TEXT, share_link);

                scrn_context.startActivity(Intent.createChooser(share, "Share link!"));

            }
        });

        holder.revoke_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(scrn_context);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Revoke....");

                // Setting Dialog Message
                alertDialog.setMessage("You are about to Revoke the Set Shared. Are you want to Revoke? ");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.error);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {



                        mListener.call_revoke(set_id,sharedDataModel.get(position).getId());
                        // Write your code here to invoke YES event
//                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
//                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return sharedDataModel.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_sharedName;
        TextView text_sharedNumber;
        ImageView revoke_shared;
        Button app_link, web_link;
        LinearLayout inactive_layout;
        TextView active_user, inactive_user;

        public ViewHolder(View itemView) {
            super(itemView);

            text_sharedName = itemView.findViewById(R.id.text_sharedName);
            text_sharedNumber = itemView.findViewById(R.id.text_sharedNumber);
            revoke_shared = itemView.findViewById(R.id.revoke_shared);
            app_link = itemView.findViewById(R.id.app_link);
            web_link = itemView.findViewById(R.id.web_link);
            inactive_layout = itemView.findViewById(R.id.inactive_layout);
            active_user = itemView.findViewById(R.id.active_user);
            inactive_user = itemView.findViewById(R.id.inactive_user);
        }
    }
    public interface SharedListInterface{
        public void call_revoke(String set_id, String assigned_to);
    }
}
