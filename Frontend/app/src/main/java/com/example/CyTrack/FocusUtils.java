package com.example.CyTrack;

import android.widget.EditText;

class FocusUtils {

    /**
     * Clear hint on focus
     * @param editText the EditText to clear the hint on focus
     * @param hint the hint to clear
     */
    static void clearHintOnFocus(EditText editText, String hint) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                editText.setHint("");
            } else {
                editText.setHint(hint);
            }
        });
    }
}