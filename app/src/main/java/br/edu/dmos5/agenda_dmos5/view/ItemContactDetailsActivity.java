package br.edu.dmos5.agenda_dmos5.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.ContactItem;
import br.edu.dmos5.agenda_dmos5.model.ItemContactDetails;
import br.edu.dmos5.agenda_dmos5.repository.ContactItemRepository;
import br.edu.dmos5.agenda_dmos5.view.adapter.ItemContactDetailsAdapter;

import static br.edu.dmos5.agenda_dmos5.model.ItemContactDetails.ITEM_CONTACT_DETAILS_KEY;

public class ItemContactDetailsActivity extends AppCompatActivity {

    private RecyclerView contactsRecyclerView;

    private ItemContactDetailsAdapter itemAdapter;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_contact_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showData();

    }

    private void showData() {
        ContactItemRepository repository = new ContactItemRepository(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Toast.makeText(this, R.string.default_error, Toast.LENGTH_SHORT);
            finish();
        }


        textView = findViewById(R.id.contacts_item_details_text_view);

        ItemContactDetails contactDetails = (ItemContactDetails) bundle.get(ITEM_CONTACT_DETAILS_KEY);

        List<ContactItem> items = repository.findByUserIdAndContactNameAndType(contactDetails.getUserId(), contactDetails.getFullName(), contactDetails.getType().toString());

        if (items.size() > 0) {
            textView.setText(getString(R.string.contact_data_msg) + " " + contactDetails.getFullName());
        } else {
            textView.setText(getString(R.string.empty_contact_data_msg) + " " + contactDetails.getFullName());
        }

        contactsRecyclerView = findViewById(R.id.contacts_item_details_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(layoutManager);

        itemAdapter = new ItemContactDetailsAdapter(items, this,contactDetails.getUserId(), contactDetails.getFullName());
        contactsRecyclerView.setAdapter(itemAdapter);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
