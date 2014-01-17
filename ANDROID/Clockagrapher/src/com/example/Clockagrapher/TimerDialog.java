package com.example.Clockagrapher;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class TimerDialog extends DialogFragment {

    long _time;
    int _pos;
    String _txt;

    private static final String TAG = "CLICKABLE_TIMER_DIALOG";

    static TimerDialog newInstance(int pos, long time, String txt) {
        TimerDialog f = new TimerDialog();

        Bundle args = new Bundle();
        args.putLong("time", time);
        args.putInt("pos", pos);
        args.putString("txt", txt);
        f.setArguments(args);

        return f;
    }

    public interface ProcessTimerListener {
        void processEvent(int pos, long time, String txt);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _time = getArguments().getLong("time");
        _pos = getArguments().getInt("pos");
        _txt = getArguments().getString("txt");
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timer_layout, container, false);
        final TimerPicker tp = new TimerPicker((NumberPicker) v.findViewById(R.id.hour_picker),
                                         (NumberPicker) v.findViewById(R.id.minute_picker),
                                         (NumberPicker) v.findViewById(R.id.second_picker));

        tp.setValue(_time);

        final EditText et = (EditText) v.findViewById(R.id.picker_edit_text);
        et.setText(_txt);

        Button ts = (Button) v.findViewById(R.id.timer_set);
        ts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProcessTimerListener activity = (ProcessTimerListener) getActivity();
                activity.processEvent(_pos, tp.getValue(), et.getText().toString());
                getDialog().dismiss();
            }
        });

        Button c = (Button) v.findViewById(R.id.timer_cancel);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return v;
    }

}