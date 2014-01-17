package com.example.Clockagrapher;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.example.Clockagrapher.SwipeAdapter.Action;

import java.util.ArrayList;

public class Clickable extends Activity implements TimerDialog.ProcessTimerListener,
    SwipeAdapter.SwipeListener {

    static long base_time;

    private ArrayList<EventItem> eventArrayLists;
    private SwipeAdapter<EventItem> eventArrayAdapter;


    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private Chronometer internalTimer;
    private long t;
    private View.OnClickListener buttonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomButton b = (CustomButton) view;
            eventArrayAdapter.add(new EventItem(b.getText(), t));
        }
    };

    private static final String TAG = "CLICKABLE_LOG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Log.d(TAG, "Clickable onCreate()");

        mPrefs = getSharedPreferences("gameTIME", Context.MODE_PRIVATE);
        editor = mPrefs.edit();

        eventArrayLists = fromEventString(mPrefs.getString("event_list", ""));
        eventArrayAdapter = new SwipeAdapter<EventItem>(this, android.R.layout.simple_list_item_1,
                                                     eventArrayLists);
        eventArrayAdapter.setSwipeListener(this);

        base_time = mPrefs.getLong("base_time", SystemClock.elapsedRealtime());

        internalTimer = (Chronometer) findViewById(R.id.timestamp);
        internalTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                    t = SystemClock.elapsedRealtime() - cArg.getBase();
                    cArg.setText(EventItem.dateString(t));
            }
        });
        internalTimer.setText("00:00:00");

        Button exitButton = (Button) findViewById(R.id.button_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                finish();
                System.exit(0);
            }
        });

        /**Button undoButton = (Button) findViewById(R.id.button_undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventArrayLists.size() != 0) {
                    eventArrayLists.remove(0);
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        }); **/


        RelativeLayout rl = (RelativeLayout) findViewById(R.id.button_holder);

        boolean firstButton = true;
        int prevButton = 0;
        for (Button b : getButtons()) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (firstButton) {
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                firstButton = false;
            } else {
                lp.addRule(RelativeLayout.BELOW, prevButton);
            }
            printInts(lp.getRules());
            b.setLayoutParams(lp);
            prevButton = View.generateViewId();
            Log.d(TAG, prevButton + "");
            Log.d(TAG, lp.getRules()[RelativeLayout.BELOW] + "");
            b.setId(prevButton);
            b.setOnClickListener(buttonHandler);
            rl.addView(b);
        }

        ListView lv = (ListView) findViewById(R.id.eventList);
        lv.setAdapter(eventArrayAdapter);

        registerForContextMenu(lv);
    }

    public void printInts(int[] ints) {
        String result = "";
        for (int i : ints) {
            result += i + ",";
        }
        Log.d(TAG, result);
    }

    @Override
    public boolean swipeAt(int pos, Action action) {
        if (action == Action.TO_LEFT) {
            eventArrayLists.remove(pos);
            eventArrayAdapter.notifyDataSetChanged();
        } else if (action == Action.TO_RIGHT) {
            FragmentManager fm = getFragmentManager();
            EventItem ev = eventArrayLists.get(pos);
            TimerDialog td = TimerDialog.newInstance(pos,ev.getOffset(), ev.getID());
            td.show(fm, "event_item_timer_edit");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        internalTimer.setBase(base_time);
        internalTimer.start();

    }

    @Override
    public void onPause() {
        super.onPause();

        editor.putString("event_list", eventString(eventArrayLists));
        editor.putLong("base_time", base_time);
        editor.commit();
    }

    @Override
    public void processEvent(int pos, long time, String txt) {
        EventItem ei = eventArrayLists.get(pos);
        ei.setOffSet(time);
        ei.setID(txt);
        eventArrayAdapter.notifyDataSetChanged();
        Log.d(TAG, pos + "\t" + time);
    }

    /** Constructs a string form of an EventItem ArrayList. */
    public static String eventString(ArrayList<EventItem> al) {
        StringBuffer result = new StringBuffer();
        for(EventItem e: al) {
            result.append(e.serialString());
            result.append("\n");
        }
        return result.toString();

    }

    /** Constructs an array of EventItems from an EventString. */
    public static ArrayList<EventItem> fromEventString(String es) {
        ArrayList<EventItem> result = new ArrayList<EventItem>();
        if (es == "") return result;
        String[] stringArray = es.split("\n");
        for (String s: stringArray) {
            result.add(new EventItem(s));
        }
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //Log.d(TAG, "dispatch()");
        return super.dispatchTouchEvent(ev);
    }


    private ArrayList<CustomButton> getButtons() {
        ArrayList<CustomButton> buttons = new ArrayList<CustomButton>();
        CustomButton c1 = new CustomButton(this);
        CustomButton c2 = new CustomButton(this);
        CustomButton c3 = new CustomButton(this);
        c1.setText("Professor Question");
        c2.setText("Student Question");
        c3.setText("Interruption");
        buttons.add(c1);
        buttons.add(c2);
        buttons.add(c3);
        return buttons;

    }
}
