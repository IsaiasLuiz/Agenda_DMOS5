package br.edu.dmos5.agenda_dmos5.view;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemeUtils;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

import java.util.LinkedList;
import java.util.List;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.Contact;
import br.edu.dmos5.agenda_dmos5.model.ContactItem;
import br.edu.dmos5.agenda_dmos5.model.ContactItemType;
import br.edu.dmos5.agenda_dmos5.model.ContactV2;
import br.edu.dmos5.agenda_dmos5.model.exceptions.EmptyFieldsException;
import br.edu.dmos5.agenda_dmos5.model.exceptions.NameAlreadyRegisteredException;
import br.edu.dmos5.agenda_dmos5.repository.ContactRepository;
import br.edu.dmos5.agenda_dmos5.repository.ContactV2Repository;

import static br.edu.dmos5.agenda_dmos5.utils.SnackbarUtils.showSnackbar;

public class NewContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ContactV2Repository contactRepository;

    private EditText nameEditText;

    private EditText landlinePhoneEditText;

    private EditText cellPhoneEditText;

    private EditText emailEditText;

    private Button cancelButton;

    private Button saveButton;

    private ImageButton btnNewEmail;

    private ImageButton btnDeleteEmail;

    private ImageButton btnNewCell;

    private ImageButton btnDeleteCell;

    private ImageButton btnNewLandline;

    private ImageButton btnDeleteLandline;

    private List<String> cells;

    private List<String> landlines;

    private List<String> emails;

    private TextView landlineResult;

    private TextView cellResult;

    private TextView emailResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        cells = new LinkedList<>();
        landlines = new LinkedList<>();
        emails = new LinkedList<>();

        nameEditText = findViewById(R.id.name_edit_text);
        landlinePhoneEditText = findViewById(R.id.landline_phone_edit_text);
        cellPhoneEditText = findViewById(R.id.cell_phone_edit_text);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);

        emailEditText = findViewById(R.id.email_edit_text);

        cellResult = findViewById(R.id.cell_result_tv);
        landlineResult = findViewById(R.id.landline_result_tv);
        emailResult = findViewById(R.id.email_result_tv);

        btnNewCell = findViewById(R.id.new_cell_image_button);
        btnDeleteCell = findViewById(R.id.delete_cell_image_button);
        btnNewLandline = findViewById(R.id.new_landline_image_button);
        btnDeleteLandline = findViewById(R.id.delete_landline_image_button);
        btnNewEmail = findViewById(R.id.new_email_image_button);
        btnDeleteEmail = findViewById(R.id.delete_email_image_button);

        btnNewCell.setOnClickListener(this);
        btnNewLandline.setOnClickListener(this);
        btnNewEmail.setOnClickListener(this);

        btnDeleteCell.setOnClickListener(this);
        btnDeleteLandline.setOnClickListener(this);
        btnDeleteEmail.setOnClickListener(this);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        contactRepository = new ContactV2Repository(getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        if (saveButton == v) {
            saveContact();
        } else if (cancelButton == v) {
            finish();
        } else if (btnNewCell == v) {
            if (!cellPhoneEditText.getText().toString().isEmpty()) {
                cells.add(cellPhoneEditText.getText().toString());
                cellResult.setText(cells.toString());
            }
        } else if (btnDeleteCell == v) {
            if(cells.size() > 0) {
                cells.remove(cells.size() - 1);
                cellResult.setText(cells.toString());
            }
        } else if (btnNewLandline == v) {
            if(!landlinePhoneEditText.getText().toString().isEmpty()) {
                landlines.add(landlinePhoneEditText.getText().toString());
                landlineResult.setText(landlines.toString());
            }
        } else if (btnDeleteLandline == v) {
            if(landlines.size() > 0) {
                landlines.remove(landlines.size() - 1);
                landlineResult.setText(landlines.toString());
            }
        } else if (btnNewEmail == v) {
            if (!emailEditText.getText().toString().isEmpty()) {
                emails.add(emailEditText.getText().toString());
                emailResult.setText(emails.toString());
            }
        } else if (btnDeleteEmail == v) {
            if(emails.size() > 0) {
                emails.remove(emails.size() - 1);
                emailResult.setText(emails.toString());
            }
        }

    }

    private void saveContact() {
        ConstraintLayout layout = findViewById(R.id.new_contact_activity);
        try {
            SharedPreferences mSharedPreferences = this.getPreferences(MODE_PRIVATE);
            mSharedPreferences = this.getSharedPreferences(getString(R.string.file_preferences), MODE_PRIVATE);
            final String userLogged = mSharedPreferences.getString(getString(R.string.key_logged_user), "");

            String name = nameEditText.getText().toString();
            if (name.isEmpty()) {
                showSnackbar(getString(R.string.empty_name_error), layout);
            } else {
                final ContactV2 contact = new ContactV2(name, getContactItems());
                try {
                    contactRepository.save(contact, userLogged);
                    finish();
                } catch (NameAlreadyRegisteredException e ) {

                    new MaterialAlertDialogBuilder(this, R.style.Theme_MaterialComponents_Dialog_Alert)
                            .setTitle("Um contato com o nome " + contact.getFullName() + " já existe")
                            .setMessage("Gostaria de mesclar?")
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    contactRepository.saveAndMerge(contact, userLogged);
                                    finish();
                                }
                            })
                            .show();
                }
            }

        } catch (EmptyFieldsException e) {
            showSnackbar(getString(R.string.empty_contact_items_error), layout);
        } catch (SQLiteConstraintException e) {
            showSnackbar(getString(R.string.duplicated_field_error), layout);
        } catch (Exception e) {
            e.printStackTrace();
            showSnackbar(getString(R.string.default_error), layout);
        }
    }

    private List<ContactItem> getContactItems() {
        if (cells.size() < 1 && landlines.size() < 1 && emails.size() < 1) {
            throw new EmptyFieldsException();
        }

        List<ContactItem> list = new LinkedList<>();

        for (String s : cells) {
            list.add(new ContactItem(s, ContactItemType.CELLPHONE));
        }
        for (String s : landlines) {
            list.add(new ContactItem(s, ContactItemType.LANDLINEPHONE));
        }
        for (String s : emails) {
            list.add(new ContactItem(s, ContactItemType.EMAIL));
        }

        return list;
    }

}
