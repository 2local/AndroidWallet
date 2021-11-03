package com.android.l2l.twolocal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.android.l2l.twolocal.R;

public class CustomButton extends AppCompatButton {
    public CustomButton(Context context) {
        super(context);
        config();
    }


    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        config();

    }

    public CustomButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        setFocusable(true);
        config();
    }

    @Override
    public void setTextSize(int unit, float size) {

    }

    private void config() {
        setClickable(true);
        setFocusable(true);
        setAllCaps(false);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.medium_text_size));
        setTransformationMethod(null);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
}
