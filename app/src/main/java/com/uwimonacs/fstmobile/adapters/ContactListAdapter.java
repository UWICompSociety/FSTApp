package com.uwimonacs.fstmobile.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uwimonacs.fstmobile.R;

import com.uwimonacs.fstmobile.models.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    public class ContactViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView number;

        public ContactViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.contact_name);
            number = (TextView) itemView.findViewById(R.id.contact_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = ContactViewHolder.this.getAdapterPosition();
                    String number = contacts.get(pos).getNumber();
                    if(!TextUtils.isEmpty(number))
                    {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + number ));
                        ctxt.startActivity(intent);  // start dialer activity
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = ContactViewHolder.this.getAdapterPosition();
                    String number = contacts.get(pos).getNumber();

                    if (!TextUtils.isEmpty(number)) {
                        ClipboardManager clipboard = (ClipboardManager)
                                ctxt.getSystemService(Context.CLIPBOARD_SERVICE);

                        ClipData clip = ClipData.newPlainText("number", number);

                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(ctxt, "Text copied", Toast.LENGTH_SHORT).show();
                    }

                    return  true;
                }
            });
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact_item,
                parent, false);

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName()); // sets name of contact
        holder.number.setText(contacts.get(position).getNumber()); // sets number of contact
    }

    @Override
    public int getItemCount() {
        return contacts.size(); //returns number of contacts
    }

    public void updateContacts(List<Contact> newContacts)
    {
        this.contacts = new ArrayList<>(newContacts);
        notifyDataSetChanged();
    }
}
