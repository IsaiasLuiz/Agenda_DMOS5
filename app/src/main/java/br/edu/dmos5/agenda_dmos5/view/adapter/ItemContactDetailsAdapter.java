package br.edu.dmos5.agenda_dmos5.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.ContactItem;

public class ItemContactDetailsAdapter extends RecyclerView.Adapter<ItemContactDetailsAdapter.ContactsItemViewHolder> {

    private List<ContactItem> contactsItemList;

    private static RecyclerItemClickListener clickListener;

    public ItemContactDetailsAdapter(List<ContactItem> contactsItemList) {
        this.contactsItemList = contactsItemList;
    }

    public void setClickListener(RecyclerItemClickListener clickListener) {
        ItemContactDetailsAdapter.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ItemContactDetailsAdapter.ContactsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        ItemContactDetailsAdapter.ContactsItemViewHolder viewHolder = new ItemContactDetailsAdapter.ContactsItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemContactDetailsAdapter.ContactsItemViewHolder holder, final int position) {
        holder.valueTextView.setText(contactsItemList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return contactsItemList.size();
    }

    public static class ContactsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView valueTextView;

        public ContactsItemViewHolder(@NonNull View itemView) {
            super(itemView);

            valueTextView = itemView.findViewById(R.id.list_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }

}
