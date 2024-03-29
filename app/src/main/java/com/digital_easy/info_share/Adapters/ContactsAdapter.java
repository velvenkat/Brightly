package com.digital_easy.info_share.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digital_easy.info_share.Modules.ContactShare;
import com.digital_easy.info_share.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends BaseAdapter {

    private List<ContactShare> contactShares = null;
    private ArrayList<ContactShare> arraylist;
    private ArrayList<String> selectedNumber = new ArrayList<String>();
    private ArrayList<String> selectedName = new ArrayList<String>();

    Context scrn_context;
    LayoutInflater inflater;
    String string;
    ContactAdapterInterface mListener;

    public ContactsAdapter(Context shareWithContacts, ArrayList<ContactShare> getContactShares,ContactAdapterInterface listener) {

        this.scrn_context = shareWithContacts;
        this.contactShares = getContactShares;
//        this.contactListFiltered = contactShares;
        this.arraylist = new ArrayList<ContactShare>();
        this.arraylist.addAll(getContactShares);
        inflater = (LayoutInflater.from(scrn_context));
        mListener=listener;

    }

    public class ViewHolder {
        TextView tvContactName, tvPhoneNumber;
        ImageView tick_selected;
        CardView contact_layout;
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
    public int getItemViewType(int position) {
        return position;
    }

    public ArrayList<String> getContact_Share(){
        return selectedNumber;
    }

    public ArrayList<String> getContactName_Share(){
        return selectedName;
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
            holder.tick_selected = view.findViewById(R.id.tick_selected);
            holder.contact_layout = view.findViewById(R.id.contact_layout);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ContactShare model_view = contactShares.get(position);
        // Set the results into TextViews
        holder.tvContactName.setText(model_view.getContactName());
        holder.tvPhoneNumber.setText(model_view.getContactNumber());
        holder.tick_selected.setImageResource(model_view.isSelected() ? R.mipmap.ic_right_tick : R.mipmap.ic_tick);

        holder.contact_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             /*   model_view.setSelected(!model_view.isSelected());
                holder.tick_selected.setImageResource(model_view.isSelected() ? R.mipmap.ic_right_tick : R.mipmap.ic_tick);
*/
             if (model_view.isSelected()) {
                    holder.tick_selected.setImageResource(R.mipmap.ic_tick);
                    model_view.setSelected(false);
                    int pos = 0;
                 String mob_no=model_view.getContactNumber();
                 String cont_name = model_view.getContactName();
                 mob_no=mob_no.replaceAll("[^0-9]","").trim();
                    for (String num : selectedNumber) {
                        if (num.equals(mob_no)) {
                            selectedNumber.remove(pos);
                            break;
                        }
                        pos++;
                    }
                 pos=0;
                 for (String name : selectedName) {
                     if (cont_name.equals(name)) {
                         selectedName.remove(pos);
                         break;
                     }
                     pos++;
                 }

                } else
                    {
                        String mob_no=model_view.getContactNumber();
                        String cont_name = model_view.getContactName();
                        mob_no=mob_no.replaceAll("[^0-9]","").trim();
                        if(mob_no.length()>=10) {
                            selectedNumber.add(mob_no);
                            selectedName.add(cont_name);
                            holder.tick_selected.setImageResource(R.mipmap.ic_right_tick);
                            model_view.setSelected(true);
                        }
                /*HashSet<String> listToSet = new HashSet<String>(selectedNumber);

                List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);*/
                    }

                       /* string = TextUtils.join(", ", selectedName);
                        Toast.makeText(scrn_context, string, Toast.LENGTH_LONG).show();
                        String string = TextUtils.join(", ", selectedNumber);
                        Toast.makeText(scrn_context, string, Toast.LENGTH_LONG).show();*/


            mListener.contactsClicked();
            }

        });







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
            for (ContactShare centerInfo : arraylist) //startsWith(constraint.toString().toUpperCase())) contains(charText.substring(0, 1).toUpperCase() + charText.substring(1).toLowerCase()))
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
    public interface ContactAdapterInterface
    {
       public void contactsClicked();
    }
}
