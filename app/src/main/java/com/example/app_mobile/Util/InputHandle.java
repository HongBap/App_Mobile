package com.example.app_mobile_phone.Util;

import android.text.InputFilter;
import android.text.Spanned;

public class InputHandle {
    public static InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };
}
