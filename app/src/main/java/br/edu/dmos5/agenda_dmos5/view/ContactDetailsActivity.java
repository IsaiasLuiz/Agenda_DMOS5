package br.edu.dmos5.agenda_dmos5.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.Contact;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView nameTextView;

    private TextView landlinePhoneTextView;

    private TextView cellPhoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTextView = findViewById(R.id.contact_details_name_text_view_2);
        landlinePhoneTextView = findViewById(R.id.contact_details_landline_phone__text_view_2);
        cellPhoneTextView = findViewById(R.id.contact_details_cell_phone__text_view_2);

        extractData();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void extractData() {
        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            Toast.makeText(this, R.string.default_error, Toast.LENGTH_SHORT);
            finish();
        }
        Contact contact = (Contact) bundle.get(Contact.CONTACT_KEY);

        nameTextView.setText(contact.getFullName());
        landlinePhoneTextView.setText(contact.getLandlinePhone());
        cellPhoneTextView.setText(contact.getCellPhone());
    }

}
