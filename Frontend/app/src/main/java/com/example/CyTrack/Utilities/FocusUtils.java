package com.example.CyTrack.Utilities;

import android.widget.EditText;

/**
 * Utility class for handling focus-related operations on EditText.
 */
public class FocusUtils {

    /**
     * Clears the hint of the EditText when it gains focus and restores it when it loses focus.
     *
     * @param editText the EditText to clear the hint on focus
     * @param hint     the hint to restore when focus is lost
     */
    public static void clearHintOnFocus(EditText editText, String hint) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.setHint("");
            } else {
                editText.setHint(hint);
            }
        });
    }
}