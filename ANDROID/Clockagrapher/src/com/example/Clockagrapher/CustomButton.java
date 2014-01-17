package com.example.Clockagrapher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import java.nio.CharBuffer;

public class CustomButton extends Button {

    public static final String TAG = "CLICKABLE_CUSTOMBUTTON";

    private String _longString;
    private String _abbrev;

    public CustomButton(Context c) { super(c);}

    public CustomButton(Context c, AttributeSet as) { super(c, as);}

    public CustomButton(Context c, AttributeSet as, int ds) { super(c, as, ds);}

    public void setText(String s) {
        _longString = s;
        setAbbrev();
        super.setText(_abbrev);
    }

    public String getText() {
        return _longString;
    }

    private void setAbbrev() {
        String[] sArray = _longString.split(" ");
        String result = "";
        for (String s: sArray) {
            result += s.substring(0, 1).toUpperCase();
        }
        _abbrev = result;
    }



}
