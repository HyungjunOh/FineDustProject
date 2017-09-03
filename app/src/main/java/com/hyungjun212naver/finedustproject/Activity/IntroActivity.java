package com.hyungjun212naver.finedustproject.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hyungjun212naver.finedustproject.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hyung on 2017-08-14.
 */

public class IntroActivity extends AppCompatActivity {

    private Context mContext;
    private Timer mRefreshTimer;

    final public int MY_PERMISSIONS_REQUEST_COARSE_LOCATION=1;

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

    private void PermissonCheck(){
        //permission check
        int permissionCheck = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        //권한이 부여되지 않았을 경우 : -1(PERMISSION_DENIED). 부여된 경우 0(PERMISSION_GRANTED)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(IntroActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(getApplicationContext(),"권한 사용을 동의하지 않으면 사용에 제한이 있을 수 있습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

}