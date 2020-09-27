package br.edu.dmos5.agenda_dmos5.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.Contact;
import br.edu.dmos5.agenda_dmos5.repository.ContactRepository;

public class ContactActivity extends AppCompatActivity {

    private List<Contact> contactList;

    private ContactRepository contactRepository;

    private ListView listViewContacts;

    private ArrayAdapter<Contact> arrayAdapterContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contactRepository = new ContactRepository(getApplicationContext());
        contactList = contactRepository.findAll();

        listViewContacts = findViewById(R.id.contacts_list_view);
        arrayAdapterContact = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactList);
        listViewContacts.setAdapter(arrayAdapterContact);

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Contact contact = contactList.get(position);
                                                        Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
                                                        intent.putExtra(Contact.CONTACT_KEY, contact);
                                                        startActivity(intent);
                                                    }
                                                }
        );

    }

    @Override
    protected void onResume() {
        contactList.clear();
        contactList.addAll(contactRepository.findAll());
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

}
