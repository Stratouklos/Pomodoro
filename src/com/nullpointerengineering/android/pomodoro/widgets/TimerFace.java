package com.nullpointerengineering.android.pomodoro.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nullpointerengineering.android.pomodoro.R;

/*
 * Created by IntelliJ IDEA.
 * User: Stratos
 * Date: 17/04/12
 * Time: 9:46 PM
 * Displays the time.
 */
public class TimerFace extends LinearLayout {

    /**
     * Time displaying layout.
     * @param context   The context in which the time will be displayed
     * @param attributeSet  The attribute set for the layout
     */
    public TimerFace(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.timer_face, this, true);
    }

    /**
     * Sets the time to display in MM:SS format
     * @param time Time in seconds
     */
    public void setTime(int time) {
        String t;
        int minutes = time/60;
        int seconds = time%60;

        if (minutes < 10) t = "0" + minutes;                // Add a 0 to keep digits balanced.
        else t = "" + minutes;
        if (seconds < 10) t = t + ":0" + seconds;           // Add a 0 to keep digits balanced.
        else t = t + ":" + seconds;
        TextView display = (TextView) findViewById(R.id.timer_text);
        display.setText(t);                              //Time in the format of mm:ss
    }

}
