package com.compilers.segcookhelper.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Contains Util methods for Activities
 */

class ActivityUtil {

    static final int MAX_DESCRIPTION_LIMIT = 500;
    static final int MIN_DESCRIPTION_LIMIT = 1;

    /**
     * Creates a neutral alert dialog with one button and displays it
     *
     * @param msg        the message to be displayed
     * @param activity   the activity to be displayed on
     * @param cancelable can be canceled
     * @param buttonText the text on the button
     */
    static void openNeutralAlertDialog(String msg, Activity activity, Boolean cancelable, String buttonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setCancelable(cancelable);

        builder.setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Tests if the string is within MAX_DESCRIPTION_LIMIT and MIN_DESCRIPTION_LIMIT
     *
     * @param string the description to test
     * @return true is it is within the limits, false otherwise
     */
    static boolean isWithinDescriptionLimits(String string) {
        return string.length() <= MAX_DESCRIPTION_LIMIT && string.length() >= MIN_DESCRIPTION_LIMIT;
    }
}
