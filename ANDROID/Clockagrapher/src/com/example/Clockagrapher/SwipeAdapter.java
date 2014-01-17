package com.example.Clockagrapher;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class SwipeAdapter<T> extends ArrayAdapter<T> {

    public static final String TAG = "CLICKABLE_ADAPTER";

    public interface SwipeListener {
        /** Boolean represents if touch event is to be consumed. */
        public boolean swipeAt(int position, Action action);
    }

    private float beginX;
    private float beginY;
    private float deltaX;
    private float deltaY;

    private Action _action;
    private int _position;
    private boolean _sameItem;
    private boolean _isDown;
    private SwipeListener _swiper;
    private View _downView;

    public static enum Action {
        TO_LEFT,
        TO_RIGHT,
        TO_UP,
        TO_DOWN,
        NONE
    }

    public void setSwipeListener(SwipeListener sl) {
        _swiper = sl;
    }

    public SwipeAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SwipeAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SwipeAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                ListView sl = (ListView) view.getParent();
                final int action = ev.getAction();
                //Log.d(TAG, _position + "");

                switch(action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (!_isDown) {
                            _isDown = true;
                            beginX = ev.getX();
                            beginY = ev.getY();
                            _position = sl.getPositionForView(view);
                            sl.setSelection(_position);
                            view.setBackgroundColor(0xFFffde7c);
                            _action = Action.NONE;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP :
                        _isDown = false;
                        int newPos = sl.getPositionForView(view);
                        boolean SAME = newPos == _position;
                        deltaX = ev.getX() - beginX;
                        deltaY = ev.getY() - beginY;
                        if (Math.abs(deltaX) > Math.abs(deltaY) ||
                           (Math.abs(deltaY) < 30 && Math.abs(deltaX) > 10)) {
                            // Assigns direction to gesture on finish
                            if (SAME) {
                                if(deltaX < 0) { _action = Action.TO_LEFT; }
                                else if (deltaX > 0) { _action = Action.TO_RIGHT; }
                                else { _action = Action.NONE; }
                            } else {
                                _action = Action.NONE;
                                sl.getSelectedView().setBackgroundColor(0xFFffd149);
                            }
                        }
                        view.setBackgroundColor(0xFFffd149);
                        Log.d(TAG, _action + "");
                        break;

                }

                if (_swiper != null) {
                    return _swiper.swipeAt(_position, _action);
                }
                return false;
            }});

        v.setMinimumHeight(100);

        return v;
    }
}
