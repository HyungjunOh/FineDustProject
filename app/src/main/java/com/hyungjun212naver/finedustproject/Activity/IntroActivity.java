package com.hyungjun212naver.finedustproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hyungjun212naver.finedustproject.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hyung on 2017-08-14.
 */

public class IntroActivity extends AppCompatActivity {

    private Context mContext;
    private Timer mRefreshTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_intro);
        initialize();

    }

    private void initialize(){
        reserveActivityChange(2*1000);
    }

    private void reserveActivityChange(long delay) {
        if(mRefreshTimer != null) {
            mRefreshTimer.cancel();
        }
        mRefreshTimer = new Timer();
        mRefreshTimer.schedule(new RefreshTimerTask(), delay);
    }

    private class RefreshTimerTask extends TimerTask {
        public RefreshTimerTask() {}

        public void run() {
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }
    }

}