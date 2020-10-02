package br.edu.dmos5.agenda_dmos5.utils;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {

    public static void showSnackbar(String message, ConstraintLayout layout) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}
