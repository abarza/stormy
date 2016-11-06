package com.abarza.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.abarza.stormy.R;

/**
 * Created by abarza on 31-10-16.
 */

public class AlertDialogFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Context context = getActivity();
    AlertDialog.Builder builder = new AlertDialog.Builder(context)
        .setTitle(R.string.error_title)
        .setMessage(R.string.error_message)
        .setPositiveButton(R.string.error_ok_button_text, null);

    AlertDialog dialog = builder.create();
    return dialog;
  }
}
