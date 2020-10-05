package br.edu.dmos5.agenda_dmos5.view.adapter;

import android.content.Context;
import android.media.Image;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.ContactItem;
import br.edu.dmos5.agenda_dmos5.model.ContactItemType;
import br.edu.dmos5.agenda_dmos5.repository.ContactItemRepository;

public class ItemContactDetailsAdapter extends RecyclerView.Adapter<ItemContactDetailsAdapter.ContactsItemViewHolder> {

    private List<ContactItem> contactsItemList;

    private static RecyclerItemClickListener clickListener;

    private Context context;

    private String userLogged;

    private String contactName;

    private ContactItemRepository repository;

    public ItemContactDetailsAdapter(List<ContactItem> contactsItemList, Context context, String userLogged, String contactName) {
        this.contactsItemList = contactsItemList;
        this.context = context;
        this.userLogged = userLogged;
        this.contactName = contactName;
        repository = new ContactItemRepository(context);
    }

    public void setClickListener(RecyclerItemClickListener clickListener) {
        ItemContactDetailsAdapter.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ItemContactDetailsAdapter.ContactsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact_details, parent, false);
        ItemContactDetailsAdapter.ContactsItemViewHolder viewHolder = new ItemContactDetailsAdapter.ContactsItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemContactDetailsAdapter.ContactsItemViewHolder holder, final int position) {
        holder.valueTextView.setText(contactsItemList.get(position).getValue());

        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.valueTextView.setVisibility(View.INVISIBLE);
                holder.editImageView.setVisibility(View.INVISIBLE);
                holder.deleteImageView.setVisibility(View.INVISIBLE);
                holder.valueEditText.setInputType(getEditTextTypeByContactType(contactsItemList.get(position).getType()));
                holder.valueEditText.setText(contactsItemList.get(position).getValue());
                holder.valueEditText.setVisibility(View.VISIBLE);
                holder.saveButton.setVisibility(View.VISIBLE);
            }
        });

        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newValue = holder.valueEditText.getText().toString();
                if (!newValue.isEmpty()) {
                    repository.update(userLogged, contactName, contactsItemList.get(position).getType(), newValue);
                    contactsItemList.get(position).setValue(newValue);
                    holder.valueTextView.setVisibility(View.VISIBLE);
                    holder.editImageView.setVisibility(View.VISIBLE);
                    holder.deleteImageView.setVisibility(View.VISIBLE);
                    holder.valueEditText.setVisibility(View.INVISIBLE);
                    holder.saveButton.setVisibility(View.INVISIBLE);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, R.string.empty_fields_error, Toast.LENGTH_SHORT);
                }
            }
        });

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.delete(userLogged, contactName, contactsItemList.get(position).getType(), contactsItemList.get(position).getValue());
                contactsItemList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsItemList.size();
    }

    public static class ContactsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView valueTextView;

        public EditText valueEditText;

        public ImageView editImageView;

        public Button saveButton;

        public ImageView deleteImageView;

        public ContactsItemViewHolder(@NonNull View itemView) {
            super(itemView);

            valueTextView = itemView.findViewById(R.id.list_item_contact_details_tv);
            valueEditText = itemView.findViewById(R.id.list_item_contact_details_et);
            editImageView = itemView.findViewById(R.id.list_item_contact_details_iv);
            saveButton = itemView.findViewById(R.id.list_item_contact_details_btn);
            deleteImageView = itemView.findViewById(R.id.list_item_contact_details_iv_delete);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }

    public int getEditTextTypeByContactType(ContactItemType type) {
        int eType = InputType.TYPE_CLASS_TEXT;

        if (type.equals(ContactItemType.CELLPHONE) || type.equals(ContactItemType.LANDLINEPHONE)) {
            eType = InputType.TYPE_CLASS_PHONE;
        }
        return eType;
    }

}
