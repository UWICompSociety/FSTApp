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
import com.uwimonacs.fstmobile.models.FAQ;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    private ArrayList<Contact> contacts;
    private final Context ctxt;
    private String filter = "";

    public class ContactViewHolder extends RecyclerView.ViewHolder  {
        final TextView name;
        final TextView number;

        public ContactViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.contact_name);
            number = (TextView) v.findViewById(R.id.contact_number);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = ContactViewHolder.this.getAdapterPosition();
                    final String number = contacts.get(pos).getNumber();
                    if (!TextUtils.isEmpty(number)) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + number ));
                        v.getContext().startActivity(intent);  // start dialer activity
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int pos = ContactViewHolder.this.getAdapterPosition();
                    final String number = contacts.get(pos).getNumber();

                    if (!TextUtils.isEmpty(number)) {
                        final ClipboardManager clipboard = (ClipboardManager)
                                v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                        final ClipData clip = ClipData.newPlainText("number", number);

                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(v.getContext(), "Text copied", Toast.LENGTH_SHORT).show();
                    }

                    return  true;
                }
            });
        }
    }

    public ContactListAdapter(Context ctxt, List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
        this.ctxt = ctxt;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact_item, parent, false);

        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName()); // sets name of contact
        holder.number.setText(contacts.get(position).getNumber()); // sets number of contact
    }

    @Override
    public int getItemCount() {
        return contacts.size(); // returns number of contacts
    }

    public void updateContacts(List<Contact> newContacts) {
        this.contacts = new ArrayList<>(newContacts);
        notifyDataSetChanged();
    }

    public void animateTo(List<Contact> models, String filter) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
        this.filter = filter;
    }

    private void applyAndAnimateRemovals(List<Contact> newModels) {
        for (int i = contacts.size() - 1; i >= 0; i--) {
            final Contact model = contacts.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Contact> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Contact model = newModels.get(i);
            if (!contacts.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Contact> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Contact model = newModels.get(toPosition);
            final int fromPosition = contacts.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Contact removeItem(int position) {
        final Contact model = contacts.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Contact model) {
        contacts.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Contact model = contacts.remove(fromPosition);
        contacts.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
