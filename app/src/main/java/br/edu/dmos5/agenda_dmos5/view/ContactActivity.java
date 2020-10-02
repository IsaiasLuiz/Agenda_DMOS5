package br.edu.dmos5.agenda_dmos5.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.Contact;
import br.edu.dmos5.agenda_dmos5.repository.ContactRepository;
import br.edu.dmos5.agenda_dmos5.view.adapter.ItemContactAdapter;
import br.edu.dmos5.agenda_dmos5.view.adapter.RecyclerItemClickListener;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Contact> contactList;

    private ContactRepository contactRepository;

    private RecyclerView contactsRecyclerView;

    private ItemContactAdapter contactAdapter;

    private FloatingActionButton buttonExit;

    private SharedPreferences mSharedPreferences;

    private String loggedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mSharedPreferences = this.getPreferences(MODE_PRIVATE);
        mSharedPreferences = this.getSharedPreferences(getString(R.string.file_preferences), MODE_PRIVATE);

        loggedUser = mSharedPreferences.getString(getString(R.string.key_logged_user), "");

        buttonExit = findViewById(R.id.exit);
        buttonExit.setOnClickListener(this);

        contactRepository = new ContactRepository(getApplicationContext());
        contactList = contactRepository.findAll(loggedUser);

        contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(layoutManager);

        contactAdapter = new ItemContactAdapter(contactList);
        contactsRecyclerView.setAdapter(contactAdapter);

        contactAdapter.setClickListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Contact contact = contactList.get(position);
                Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
                intent.putExtra(Contact.CONTACT_KEY, contact);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        contactList.clear();
        contactList.addAll(contactRepository.findAll(loggedUser));
        contactAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_contact_menu:
                Intent in = new Intent(this, NewContactActivity.class);
                startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonExit) {
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putString(getString(R.string.key_logged_user), "");
            mEditor.commit();
            Intent intent = new Intent(this, NewUserActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
