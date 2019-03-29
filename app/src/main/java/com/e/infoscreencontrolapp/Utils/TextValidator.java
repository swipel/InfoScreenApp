package com.e.infoscreencontrolapp.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class TextValidator implements TextWatcher {
    private final TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text, Boolean validated);

    //Used for validating user input
    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        boolean validated = false;

        if (text.length() == 0){
            textView.setError("Feltet må ikke være tomt");
        }

        if (textView.getError() == null){
            validated = true;
        }
        validate(textView, text, validated);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {}
}