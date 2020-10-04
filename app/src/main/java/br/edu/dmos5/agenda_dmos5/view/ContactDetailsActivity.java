package br.edu.dmos5.agenda_dmos5.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.ContactItemType;
import br.edu.dmos5.agenda_dmos5.model.ContactV2;
import br.edu.dmos5.agenda_dmos5.model.ItemContactDetails;

import static br.edu.dmos5.agenda_dmos5.model.ItemContactDetails.ITEM_CONTACT_DETAILS_KEY;

public class ContactDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nameTextView;

    private Button btnShowCells;

    private Button btnShowLandlines;

    private Button btnShowEmails;

    private SharedPreferences mSharedPreferences;

    private String loggedUser;

    private String contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSharedPreferences = this.getPreferences(MODE_PRIVATE);
        mSharedPreferences = this.getSharedPreferences(getString(R.string.file_preferences), MODE_PRIVATE);

        loggedUser = mSharedPreferences.getString(getString(R.string.key_logged_user), "");

        nameTextView = findViewById(R.id.contact_details_name_text_view);

        btnShowCells = findViewById(R.id.show_cell_phones);
        btnShowCells.setOnClickListener(this);
        btnShowLandlines = findViewById(R.id.show_landline_phones);
        btnShowLandlines.setOnClickListener(this);
        btnShowEmails = findViewById(R.id.show_emails);
        btnShowEmails.setOnClickListener(this);

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
        contactName = bundle.getString(ContactV2.CONTACT_NAME_KEY);
        nameTextView.setText(contactName);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ItemContactDetailsActivity.class);

        if (v == btnShowCells) {
            ItemContactDetails itemContactDetails = new ItemContactDetails(contactName, ContactItemType.CELLPHONE, loggedUser);
            intent.putExtra(ITEM_CONTACT_DETAILS_KEY, itemContactDetails);

        } else if(v == btnShowLandlines) {
            ItemContactDetails itemContactDetails = new ItemContactDetails(contactName, ContactItemType.LANDLINEPHONE, loggedUser);
            intent.putExtra(ITEM_CONTACT_DETAILS_KEY, itemContactDetails);

        } else if(v == btnShowEmails) {
            ItemContactDetails itemContactDetails = new ItemContactDetails(contactName, ContactItemType.EMAIL, loggedUser);
            intent.putExtra(ITEM_CONTACT_DETAILS_KEY, itemContactDetails);

        }
        startActivity(intent);
    }
}
