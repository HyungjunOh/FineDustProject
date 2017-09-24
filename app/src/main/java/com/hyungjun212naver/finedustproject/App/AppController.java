package com.hyungjun212naver.finedustproject.App;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hyungjun212naver.finedustproject.Activity.IntroActivity;
import com.hyungjun212naver.finedustproject.Activity.MainActivity;
import com.hyungjun212naver.finedustproject.R;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;


/**
 * Created by hyung on 2017-04-14.
 */

public class AppController extends Application implements BootstrapNotifier{

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    private RegionBootstrap regionBootstrap;

    private static final String UUID1 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000015"; //우리 비콘 UUID

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers()
                .add(new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // iBeacon 레이아웃으로 설정
        //beaconManager.setBackgroundScanPeriod(10000);
        beaconManager.setBackgroundMode(true);

        Region region = new Region("backgroundRegion", Identifier.parse(UUID1),null,null);
        regionBootstrap = new RegionBootstrap(this, region);
    }

    public static synchronized AppController getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "didEnterRegion 함수 호출됨 " + " UUID : " + region.getId1() + "Major : " + region.getId2() + " Minor : " + region.getId3());
//        Toast.makeText(getApplicationContext(),"비콘 보인당",Toast.LENGTH_SHORT).show();
        sendNotification();
//        regionBootstrap.disable();

        Intent intent = new Intent(this, IntroActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "didExitRegion 함수 호출됨 : 안보임");
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        Log.d(TAG, "didDetermineStateForRegion 함수 호출됨 " + " UUID : " + region.getId1() + " Major : " + region.getId2() + " Minor : " + region.getId3());
        Log.d(TAG, i+"");
    }

    public void showNotification(String title, String message){
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this,0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }

    //알림창 생성, 추후에 변경
    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Beacon Reference Application")
                        .setContentText("An beacon is nearby.")
                        .setSmallIcon(R.drawable.faceimg_soso);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, IntroActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}