package com.angik.dianahost.Utility;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.angik.dianahost.R;

public class AlertDialogUtility {

    public interface RetryButtonClickListener {
        void onRetryButtonClicked();
    }


    public static void showAlertDialog(Activity context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
