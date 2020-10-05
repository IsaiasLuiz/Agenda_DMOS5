package br.edu.dmos5.agenda_dmos5.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.repository.ContactV2Repository;

public class ItemContactAdapter extends RecyclerView.Adapter<ItemContactAdapter.ContactsViewHolder> {

    private List<String> contactsNameList;

    private String userLogged;

    private Context context;

    private ContactV2Repository repository;

    private static RecyclerItemClickListener clickListener;

    public ItemContactAdapter(List<String> contactsNameList, Context context, String userLogged) {
        this.contactsNameList = contactsNameList;
        this.userLogged = userLogged;
        repository = new ContactV2Repository(context);
        this.context = context;
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
    public void onBindViewHolder(@NonNull final ContactsViewHolder holder, final int position) {
        holder.nameTextView.setText(contactsNameList.get(position));

        if (repository.isFavorite(userLogged, contactsNameList.get(position))) {
            holder.favoriteImage.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.favoriteImage.setImageResource(R.drawable.ic_not_favorite);
        }

        holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.favoriteContact(userLogged, contactsNameList.get(position));
                notifyDataSetChanged();
            }
        });

        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.delete(userLogged, contactsNameList.get(position));
                contactsNameList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.nameTextView.setVisibility(View.INVISIBLE);
                holder.editImageView.setVisibility(View.INVISIBLE);
                holder.deleteImage.setVisibility(View.INVISIBLE);
                holder.favoriteImage.setVisibility(View.INVISIBLE);
                holder.valueEditText.setText(contactsNameList.get(position));
                holder.valueEditText.setVisibility(View.VISIBLE);
                holder.saveButton.setVisibility(View.VISIBLE);
            }
        });

        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newValue = holder.valueEditText.getText().toString();
                if (!newValue.isEmpty()) {
                    repository.update(userLogged, contactsNameList.get(position));
                    String s = contactsNameList.get(position);
                    s = newValue;
                    holder.nameTextView.setVisibility(View.VISIBLE);
                    holder.favoriteImage.setVisibility(View.VISIBLE);
                    holder.deleteImage.setVisibility(View.VISIBLE);
                    holder.editImageView.setVisibility(View.VISIBLE);
                    holder.valueEditText.setVisibility(View.INVISIBLE);
                    holder.saveButton.setVisibility(View.INVISIBLE);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, R.string.empty_fields_error, Toast.LENGTH_SHORT);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsNameList.size();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView;

        public ImageView favoriteImage;

        public ImageView deleteImage;

        public EditText valueEditText;

        public ImageView editImageView;

        public Button saveButton;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.list_item_name);
            favoriteImage = itemView.findViewById(R.id.item_list_favorite);
            deleteImage = itemView.findViewById(R.id.item_list_delete);
            editImageView = itemView.findViewById(R.id.list_item_iv);
            saveButton = itemView.findViewById(R.id.list_item_btn);
            valueEditText = itemView.findViewById(R.id.list_item_contact_et);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }

}
