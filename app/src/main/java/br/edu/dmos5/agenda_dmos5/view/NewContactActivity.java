package br.edu.dmos5.agenda_dmos5.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.Contact;
import br.edu.dmos5.agenda_dmos5.model.exceptions.EmptyFieldsException;
import br.edu.dmos5.agenda_dmos5.repository.ContactRepository;

import static br.edu.dmos5.agenda_dmos5.utils.SnackbarUtils.showSnackbar;

public class NewContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ContactRepository contactRepository;

    private EditText nameEditText;

    private EditText landlinePhoneEditText;

    private EditText cellPhoneEditText;

    private Button cancelButton;

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        nameEditText = findViewById(R.id.name_edit_text);
        landlinePhoneEditText = findViewById(R.id.landline_phone_edit_text);
        cellPhoneEditText = findViewById(R.id.cell_phone_edit_text);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        contactRepository = new ContactRepository(getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        if (saveButton == v) {
            saveContact();
        } else if (cancelButton == v) {
            finish();
        }
    }

    private void saveContact() {
        ConstraintLayout layout = findViewById(R.id.new_contact_activity);
        try {
            Contact contact = getContactByFieldsValues();
            contactRepository.save(contact);
            finish();
        } catch (EmptyFieldsException e) {
            showSnackbar(getString(R.string.empty_fields_error), layout);
        } catch (Exception e) {
            showSnackbar(getString(R.string.default_error), layout);
        }
    }

    private Contact getContactByFieldsValues() {
        String name = nameEditText.getText().toString();
        String landlinePhone = landlinePhoneEditText.getText().toString();
        String cellPhone = cellPhoneEditText.getText().toString();

        if (name.isEmpty() || landlinePhone.isEmpty() || cellPhone.isEmpty()) {
            throw new EmptyFieldsException();
        }

        SharedPreferences mSharedPreferences = this.getPreferences(MODE_PRIVATE);
        mSharedPreferences = this.getSharedPreferences(getString(R.string.file_preferences), MODE_PRIVATE);

        return new Contact(name, landlinePhone, cellPhone, mSharedPreferences.getString(getString(R.string.key_logged_user), ""));
    }

}
