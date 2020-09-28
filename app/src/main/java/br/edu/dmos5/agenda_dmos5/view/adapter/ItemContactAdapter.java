package br.edu.dmos5.agenda_dmos5.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.Contact;

public class ItemContactAdapter extends RecyclerView.Adapter<ItemContactAdapter.ContactsViewHolder> {

    private List<Contact> contactsList;

    private static RecyclerItemClickListener clickListener;

    public ItemContactAdapter(List<Contact> contactsList) {
        this.contactsList = contactsList;
    }

    public void setClickListener(RecyclerItemClickListener clickListener) {
        ItemContactAdapter.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        ContactsViewHolder viewHolder = new ContactsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, final int position) {
        holder.nameTextView.setText(contactsList.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.list_item_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }

}
