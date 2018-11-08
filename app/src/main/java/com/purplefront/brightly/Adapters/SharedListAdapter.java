package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    String share_access = "";
    int position;
    EditSetInfo setInfo;
    String share_id;


    public SharedListAdapter(Context editSetInfo, ArrayList<SharedDataModel> sharedDataModel, String set_id, String share_link, SharedListInterface listener, EditSetInfo setInfo) {

        this.scrn_context = editSetInfo;
        this.sharedDataModel = sharedDataModel;
        this.set_id = set_id;
        this.share_link = share_link;
        this.setInfo = setInfo;
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

        this.position = position;

        SharedDataModel sharedDataModels = sharedDataModel.get(position);
        String share_Id = sharedDataModels.getId();

        this.share_id = share_Id;

        if (sharedDataModels.getName() != null) {
            holder.text_sharedName.setText(sharedDataModels.getName());
        }
       /* if (sharedDataModels.getPhone() != null) {
            holder.text_sharedNumber.setText(sharedDataModels.getPhone());
        }*/

        String share_access = sharedDataModels.getShare_access();
        this.share_access = share_access;

        switch (share_access) {
            case "0":
                holder.slcted_share_access.setImageResource(R.mipmap.can_view);
                break;
            case "1":
                holder.slcted_share_access.setImageResource(R.mipmap.can_share);
                break;
            default:
                holder.slcted_share_access.setImageResource(R.drawable.pencil_edit_button);
                break;
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


        holder.layout_share_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareSettingDialog(share_Id, sharedDataModel.get(position).getShare_access());
            }
        });


    }

    private void shareSettingDialog(String share_Id, String share_accesss)
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(scrn_context,  R.style.MaterialDialogSheet);
        View mView = inflater.inflate(R.layout.dialog_share_settings, null);

        TextView btn_dismiss = (TextView) mView.findViewById(R.id.btn_dismiss);
        TextView btn_done = (TextView) mView.findViewById(R.id.btn_done);

        CheckedTextView text_canView = (CheckedTextView) mView.findViewById(R.id.text_canView);
        CheckedTextView text_canshare = (CheckedTextView) mView.findViewById(R.id.text_canshare);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 30);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);


        switch (share_accesss) {
            case "0":
                text_canView.setCheckMarkDrawable(R.mipmap.ic_selection);
                text_canshare.setCheckMarkDrawable(R.mipmap.ic_tick);
                break;
            case "1":
                text_canshare.setCheckMarkDrawable(R.mipmap.ic_selection);
                text_canView.setCheckMarkDrawable(R.mipmap.ic_tick);
                break;
            default:
                text_canView.setCheckMarkDrawable(R.mipmap.ic_tick);
                text_canshare.setCheckMarkDrawable(R.mipmap.ic_tick);

                break;
        }


        text_canView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_canView.setCheckMarkDrawable(R.mipmap.ic_selection);
                text_canshare.setCheckMarkDrawable(R.mipmap.ic_tick);
                share_access = "0";

//                Toast.makeText(scrn_context, share_access, Toast.LENGTH_LONG).show();


            }
        });

        text_canshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_canshare.setCheckMarkDrawable(R.mipmap.ic_selection);
                text_canView.setCheckMarkDrawable(R.mipmap.ic_tick);
                share_access = "1";

//                Toast.makeText(scrn_context, share_access, Toast.LENGTH_LONG).show();

            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setInfo.api_call_share_access_update(share_access, share_Id);
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sharedDataModel.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_sharedName;
//        TextView text_sharedNumber;
        ImageView revoke_shared, slcted_share_access;
        Button app_link, web_link;
        LinearLayout inactive_layout, layout_share_setting;
        TextView active_user, inactive_user;

        public ViewHolder(View itemView) {
            super(itemView);

            text_sharedName = itemView.findViewById(R.id.text_sharedName);
//            text_sharedNumber = itemView.findViewById(R.id.text_sharedNumber);
            revoke_shared = itemView.findViewById(R.id.revoke_shared);
            app_link = itemView.findViewById(R.id.app_link);
            web_link = itemView.findViewById(R.id.web_link);
            inactive_layout = itemView.findViewById(R.id.inactive_layout);
            layout_share_setting = itemView.findViewById(R.id.layout_share_setting);
            active_user = itemView.findViewById(R.id.active_user);
            inactive_user = itemView.findViewById(R.id.inactive_user);
            slcted_share_access = itemView.findViewById(R.id.slcted_share_access);
        }
    }
    public interface SharedListInterface{
        public void call_revoke(String set_id, String assigned_to);
    }
}
