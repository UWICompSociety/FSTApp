package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 6/27/2016.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {


    public static class ContactViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView number;
        public ContactViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.contact_name);
            number = (TextView) itemView.findViewById(R.id.contact_number);
        }
    }

    private ArrayList<Contact> contacts;
    private Context ctxt;

    public ContactListAdapter(Context ctxt, List<Contact> contacts)
    {
        this.contacts = new ArrayList<>(contacts);
        this.ctxt = ctxt;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact_item,parent,false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        holder.name.setText(contacts.get(position).getName()); //sets name of contact
        holder.number.setText(contacts.get(position).getNumber()); //sets number of contact

    }

    @Override
    public int getItemCount() {
        return contacts.size(); //returns number of contacts
    }
}
