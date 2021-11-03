package com.android.l2l.twolocal.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Input text watcher for EditText
 * to make simpler implementation
 */
public abstract class InputTextWatcher implements TextWatcher {

    private EditText editText;
    boolean interrupt = true;

    public InputTextWatcher(EditText editText) {
        this.editText = editText;
    }

    public InputTextWatcher() {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (interrupt) {
            interrupt = false;
            textChanged(charSequence.toString());
            if (editText != null) 
                editText.setSelection(editText.getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        interrupt = true;
    }

    public abstract void textChanged(String text);
}
