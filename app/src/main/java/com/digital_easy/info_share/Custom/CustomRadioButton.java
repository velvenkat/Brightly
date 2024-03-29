package com.digital_easy.info_share.Custom;

import android.content.Context;
import android.widget.RadioGroup;

public class CustomRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    public CustomRadioButton(Context context) {
        super(context);
    }
    // Implement necessary constructors

    @Override
    public void toggle() {
        if(isChecked()) {
            if(getParent() instanceof RadioGroup) {
                ((RadioGroup)getParent()).clearCheck();
            }
        } else {
            setChecked(true);
        }
    }
}