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
import android.widget.ImageView;
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
    SharedListInterface mListener;


    public SharedListAdapter(Context editSetInfo, ArrayList<SharedDataModel> sharedDataModel, String set_id,SharedListInterface listener) {

        this.scrn_context = editSetInfo;
        this.sharedDataModel = sharedDataModel;
        this.set_id = set_id;
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

        public ViewHolder(View itemView) {
            super(itemView);

            text_sharedName = itemView.findViewById(R.id.text_sharedName);
            text_sharedNumber = itemView.findViewById(R.id.text_sharedNumber);
            revoke_shared = itemView.findViewById(R.id.revoke_shared);
        }
    }
    public interface SharedListInterface{
        public void call_revoke(String set_id, String assigned_to);
    }
}
