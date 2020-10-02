package br.edu.dmos5.agenda_dmos5.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import br.edu.dmos5.agenda_dmos5.R;
import br.edu.dmos5.agenda_dmos5.model.User;
import br.edu.dmos5.agenda_dmos5.model.exceptions.EmptyFieldsException;
import br.edu.dmos5.agenda_dmos5.model.exceptions.UserAlreadyRegisteredException;
import br.edu.dmos5.agenda_dmos5.repository.ContactRepository;
import br.edu.dmos5.agenda_dmos5.repository.UserRepository;

import static br.edu.dmos5.agenda_dmos5.utils.SnackbarUtils.showSnackbar;

public class NewUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText;

    private EditText passwordEditText;

    private Button buttonRegister;

    private Button buttonLogin;

    private Button buttonClear;

    private CheckBox rememberCheckBox;

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    private UserRepository userRepository;

    private ContactRepository contactRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        userRepository = new UserRepository(getApplicationContext());
        contactRepository = new ContactRepository(getApplicationContext());

        emailEditText = findViewById(R.id.new_user_name_edit_text);
        passwordEditText = findViewById(R.id.new_user_password_edit_text);

        rememberCheckBox = findViewById(R.id.remember_login);

        buttonRegister = findViewById(R.id.btn_register_new_user);
        buttonRegister.setOnClickListener(this);

        buttonLogin = findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);

        buttonClear = findViewById(R.id.btn_clear_fields);
        buttonClear.setOnClickListener(this);

        mSharedPreferences = this.getPreferences(MODE_PRIVATE);
        mSharedPreferences = this.getSharedPreferences(getString(R.string.file_preferences), MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

    }

    @Override
    protected void onResume() {
        validPreferences();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            saveUser();
        } else if (v == buttonLogin) {
            login();
        } else if (v == buttonClear) {
            clearFields();
        }
    }

    private void login() {
        ConstraintLayout layout = findViewById(R.id.user_activity);
        try {
            User user = getFieldsValues();
            boolean goIn = userRepository.login(user);
            savePreferences(user);
            if (goIn) {
                saveLoggedUser(user);
                Intent intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                finish();
            }

            showSnackbar(getString(R.string.invalid_credencials_error), layout);
        } catch (EmptyFieldsException e) {
            showSnackbar(getString(R.string.empty_fields_error), layout);
        } catch (Exception e) {
            showSnackbar(getString(R.string.default_error), layout);
        }

    }

    private void saveUser() {
        ConstraintLayout layout = findViewById(R.id.user_activity);
        try {
            User user = getFieldsValues();
            userRepository.save(user);
            boolean isFirst = mSharedPreferences.getBoolean(getString(R.string.key_first_user), true);
            if (isFirst) {
                contactRepository.updateByFirstUser(user);
                mEditor.putBoolean(getString(R.string.key_first_user), false);
                mEditor.commit();
            }
            showSnackbar(getString(R.string.user_saved_msg), layout);
        } catch (UserAlreadyRegisteredException e) {
            showSnackbar(getString(R.string.user_already_registered_error), layout);
        } catch (EmptyFieldsException e) {
            showSnackbar(getString(R.string.empty_fields_error), layout);
        } catch (Exception e) {
            showSnackbar(getString(R.string.default_error), layout);
        }
    }

    private User getFieldsValues() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            throw new EmptyFieldsException();
        }

        return new User(email, password);
    }

    private void clearFields() {
        emailEditText.setText("");
        passwordEditText.setText("");
        rememberCheckBox.setChecked(false);
    }

    private void saveLoggedUser(User user) {
        mEditor.putString(getString(R.string.key_logged_user), user.getEmail());
        mEditor.commit();
    }

    private void savePreferences(User user) {
        if (rememberCheckBox.isChecked()) {
            mEditor.putString(getString(R.string.key_user), user.getEmail());
            mEditor.commit();
            mEditor.putString(getString(R.string.key_password), user.getPassword());
            mEditor.commit();
            mEditor.putBoolean(getString(R.string.key_remember), true);
            mEditor.commit();
        } else {
            mEditor.putString(getString(R.string.key_user), "");
            mEditor.commit();
            mEditor.putString(getString(R.string.key_password), "");
            mEditor.commit();
            mEditor.putBoolean(getString(R.string.key_remember), false);
            mEditor.commit();
        }
    }

    private void validPreferences() {
        String email = mSharedPreferences.getString(getString(R.string.key_user), "");
        String password = mSharedPreferences.getString(getString(R.string.key_password), "");
        boolean remember = mSharedPreferences.getBoolean(getString(R.string.key_remember), false);
        if (remember) {
            emailEditText.setText(email);
            passwordEditText.setText(password);
            rememberCheckBox.setChecked(true);
        }
    }

}
