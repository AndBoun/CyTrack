package com.example.CyTrack.Utilities;

import android.widget.EditText;

public class FocusUtils {

    /**
     * Clear hint on focus
     * @param editText the EditText to clear the hint on focus
     * @param hint the hint to clear
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