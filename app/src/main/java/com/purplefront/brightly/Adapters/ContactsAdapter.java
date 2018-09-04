package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purplefront.brightly.Activities.ShareWithContacts;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    List<ContactShare> contactShares;
    Activity context;
    LayoutInflater inflater;

    public ContactsAdapter(ShareWithContacts shareWithContacts, List<ContactShare> contactShares) {

        this.context = shareWithContacts;
        this.contactShares = contactShares;
        inflater = (LayoutInflater.from(context));

    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.items_contacts_list, parent, false);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {

        ContactShare contactShare = contactShares.get(position);

        if (contactShare.getContactName() != null) {
            holder.tvContactName.setText(contactShare.getContactName());
        }
        if (contactShare.getContactNumber() != null) {
            holder.tvPhoneNumber.setText(contactShare.getContactNumber());
        }
    }

    @Override
    public int getItemCount() {
        return contactShares.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvContactName, tvPhoneNumber;
        LinearLayout contact_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);

            contact_layout = itemView.findViewById(R.id.contact_layout);
        }
    }
}
