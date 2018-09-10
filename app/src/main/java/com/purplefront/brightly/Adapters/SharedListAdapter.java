package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.purplefront.brightly.Activities.BaseActivity;
import com.purplefront.brightly.Activities.EditSetInfo;
import com.purplefront.brightly.Activities.ShareWithContacts;
import com.purplefront.brightly.Modules.SharedDataModel;
import com.purplefront.brightly.R;

import java.util.ArrayList;
import java.util.List;

public class SharedListAdapter extends RecyclerView.Adapter<SharedListAdapter.ViewHolder> {
    ArrayList<SharedDataModel> sharedDataModels;
    Activity context;
    LayoutInflater inflater;
    String set_id;


    public SharedListAdapter(EditSetInfo editSetInfo, ArrayList<SharedDataModel> sharedDataModels, String set_id) {

        this.context = editSetInfo;
        this.sharedDataModels = sharedDataModels;
        this.set_id = set_id;
        inflater = (LayoutInflater.from(context));
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

        SharedDataModel sharedDataModel = sharedDataModels.get(position);

        if (sharedDataModel.getName() != null) {
            holder.text_sharedName.setText(sharedDataModel.getName());
        }
        if (sharedDataModel.getPhone() != null) {
            holder.text_sharedNumber.setText(sharedDataModel.getPhone());
        }

        holder.revoke_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Revoke....");

                // Setting Dialog Message
                alertDialog.setMessage("You are about to Revoke the Set Shared. Are you want to Revoke? ");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.error);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {


                        ((EditSetInfo)context).getRevokeSet(set_id, sharedDataModel.getPhone());
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
        return sharedDataModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_sharedName;
        TextView text_sharedNumber;
        ImageView revoke_shared;

        public ViewHolder(View itemView) {
            super(itemView);

            text_sharedName = itemView.findViewById(R.id.text_sharedName);
            text_sharedNumber = itemView.findViewById(R.id.text_sharedNumber);
            revoke_shared = itemView.findViewById(R.id.revoke_shared);
        }
    }
}
