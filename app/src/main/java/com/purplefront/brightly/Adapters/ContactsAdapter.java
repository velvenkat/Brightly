package com.purplefront.brightly.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purplefront.brightly.Activities.ShareWithContacts;
import com.purplefront.brightly.Modules.ContactShare;
import com.purplefront.brightly.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactsAdapter extends BaseAdapter {

    private List<ContactShare> contactShares = null;
    private ArrayList<ContactShare> arraylist;
    Activity context;
    LayoutInflater inflater;

    public ContactsAdapter(ShareWithContacts shareWithContacts, ArrayList<ContactShare> getContactShares) {

        this.context = shareWithContacts;
        this.contactShares = getContactShares;
//        this.contactListFiltered = contactShares;
        this.arraylist = new ArrayList<ContactShare>();
        this.arraylist.addAll(getContactShares);
        inflater = (LayoutInflater.from(context));

    }

    public class ViewHolder {
        TextView tvContactName, tvPhoneNumber;
        LinearLayout contact_layout;
    }

  /*  @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {

        ContactShare contactShare = contactListFiltered.get(position);

        if (contactShare.getContactName() != null) {
            holder.tvContactName.setText(contactShare.getContactName());
        }
        if (contactShare.getContactNumber() != null) {
            holder.tvPhoneNumber.setText(contactShare.getContactNumber());
        }
    }*/


    @Override
    public int getCount() {
        return contactShares.size();
    }

    @Override
    public ContactShare getItem(int i) {
        return contactShares.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.items_contacts_list, null);
            // Locate the TextViews in listview_item.xml
            holder.tvContactName = view.findViewById(R.id.tvContactName);
            holder.tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);

            holder.contact_layout = view.findViewById(R.id.contact_layout);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tvContactName.setText(contactShares.get(position).getContactName());
        holder.tvPhoneNumber.setText(contactShares.get(position).getContactNumber());

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        //charText = charText.toLowerCase(Locale.getDefault());
        contactShares.clear();
        if (charText.length() == 0) {
            contactShares.addAll(arraylist);
        }
        else
        {
            for (ContactShare centerInfo : arraylist)
            {
                if (centerInfo.getContactName()!=null && centerInfo.getContactName().toLowerCase().contains(charText))
                {
                    contactShares.add(centerInfo);
                }
               else if (centerInfo.getContactNumber()!=null && centerInfo.getContactNumber().contains(charText))
                {
                    contactShares.add(centerInfo);
                }
            }
        }
        notifyDataSetChanged();
    }
}
