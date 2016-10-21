package com.done.extractapk.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.done.extractapk.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by YZD on 2016/10/19.
 */

public class TwoEndsProgress extends RelativeLayout {

    private static final int ANIM_TIME = 700;

    private int timespace_hanlder = 20;
    private int times_total;
    private int times_current;

    private Timer mTimer;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private float leftSpeed, rightSpeed;
    private int leftShow,rightShow;

    private TextView leftText, rightText;
    private ProgressBar leftBar, rightBar;


    public TwoEndsProgress(Context context) {
        super(context);
        initView();
    }

    public TwoEndsProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TwoEndsProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.two_ends_progress, null);
//        addView(view);
        leftText = (TextView) view.findViewById(R.id.left_progress);
        rightText = (TextView) view.findViewById(R.id.right_progress);
        leftBar = (ProgressBar) view.findViewById(R.id.left_bar);
        rightBar = (ProgressBar) view.findViewById(R.id.right_bar);
    }

    /**
     * 设置比分值，不带有动画效果
     * @param leftValue
     * @param rightValue
     */
    public void setRate(float leftValue, float rightValue) {
        if ((leftValue + rightValue) == 0) {
            setVisibility(GONE);
            return;
        }
        resetTimer();
        setVisibility(VISIBLE);
        leftBar.setProgress(0);
        rightBar.setProgress(0);
        leftShow = (int)(leftValue / (leftValue + rightValue) * 100);
        rightShow = 100 - leftShow;
        leftText.setText(String.valueOf(leftShow) + "%");
        rightText.setText(String.valueOf(rightShow) + "%");

        leftBar.setProgress(leftShow);
        rightBar.setProgress((100 - leftBar.getProgress()));

    }

    public void setRateWithAnim(float leftValue, float rightValue){
        setRate(leftValue,rightValue);
        leftSpeed = leftValue / ANIM_TIME * timespace_hanlder;
        rightSpeed = rightValue / ANIM_TIME * timespace_hanlder;
        times_total = ANIM_TIME / timespace_hanlder;
        mTimer = new Timer();
        mTimer.schedule(new BarTimerTask(), 0, timespace_hanlder);
    }

    private void resetTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
            times_current = 0;
        }
    }

    private class BarTimerTask extends TimerTask{

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    times_current++;
                    Log.e("tag", times_current + "   " + (int) (times_current * leftSpeed));
                    leftBar.setProgress((int) (times_current * leftSpeed));
                    rightBar.setProgress((int) (times_current * rightSpeed));
                    if (times_current >= times_total) {
                        leftBar.setProgress(leftShow);
                        rightBar.setProgress((100 - leftBar.getProgress()));
                        resetTimer();
                    }
                }
            });
        }
    }

}
