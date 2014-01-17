package com.example.Clockagrapher;


import android.util.Log;
import android.widget.NumberPicker;

public class TimerPicker {

    private NumberPicker _hour;
    private NumberPicker _min;
    private NumberPicker _sec;

    public static final int HOUR = 3600 * 1000;
    public static final int MINUTE = 60 * 1000;
    public static final int SECS = 1000;

    private final String TAG = "CLICKABLE_TIMER_PICKER";

    private NumberPicker.Formatter FORMAT = new NumberPicker.Formatter() {

        public String format(int value) {
            if (value > 60 || value < 0) { return "xx"; }
            else if (value > 9) { return value + ""; }
            else { return "0" + value; }
        }
    };

    public TimerPicker(NumberPicker hour, NumberPicker min, NumberPicker sec) {
        _hour = hour;
        _min = min;
        _sec = sec;

        setFormat(FORMAT);

        _hour.setMinValue(00);
        _hour.setMaxValue(23);

        _min.setMinValue(00);
        _min.setMaxValue(59);

        _sec.setMinValue(00);
        _sec.setMaxValue(59);

    }

    public void setFormat(NumberPicker.Formatter format) {
        _hour.setFormatter(format);
        _min.setFormatter(format);
        _sec.setFormatter(format);
    }

    public void setValue(long offset) {
        _hour.setValue(getHour(offset));
        _min.setValue(getMin(offset));
        _sec.setValue(getSec(offset));

        Log.d(TAG, getHour(offset) + "\t" + getMin(offset) + "\t" + getSec(offset));
    }

    public int getHour(long offset) { return ((int) offset / HOUR); }

    public int getMin(long offset) { return ((int) (offset % HOUR) / MINUTE);}

    public int getSec(long offset) { return ((int) (offset % MINUTE) / SECS);}

    public long getValue() { return (_sec.getValue() +
                                    (_min.getValue() * 60) +
                                    (_hour.getValue() * 3600)) * 1000;}


}
