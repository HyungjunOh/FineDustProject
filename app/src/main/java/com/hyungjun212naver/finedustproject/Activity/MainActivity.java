package com.hyungjun212naver.finedustproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyungjun212naver.finedustproject.Fragment.BeaconSrcFragment;
import com.hyungjun212naver.finedustproject.Fragment.ForecastFragment;
import com.hyungjun212naver.finedustproject.Fragment.GraphFragment;
import com.hyungjun212naver.finedustproject.Fragment.HomeFragment;
import com.hyungjun212naver.finedustproject.Fragment.MapFragment;
import com.hyungjun212naver.finedustproject.Fragment.PortableFragment;
import com.hyungjun212naver.finedustproject.Fragment.SettingFragment;
import com.hyungjun212naver.finedustproject.Helper.SQLiteHandler;
import com.hyungjun212naver.finedustproject.Helper.SessionManager;
import com.hyungjun212naver.finedustproject.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;
    private TextView nav_hd_name;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    public static int countBackFlag;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backpressedTime = 0;

    private static final String TAG_HOME = "home";
    private static final String TAG_OPT_1 = "forecast";
    private static final String TAG_OPT_2 = "map";
    private static final String TAG_OPT_3 = "portable";
    private static final String TAG_OPT_4 = "graph";
    private static final String TAG_OPT_5 = "beaconsrc";
    private static final String TAG_OPT_6 = "setting";
    public static String CURRENT_TAG = TAG_HOME;

    private String[] activityTitles;

//    public static String LOGINSTATE = Constants.LOGIN.LOGINFAIL;
    private static String USER_ID = null;
    private static String LOGIN_ID;


    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    //login session
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initMainActivity(savedInstanceState);
    }

    private void initMainActivity(Bundle savedInstanceState){

        countBackFlag = 0;

        Log.e("MainActivity", "start");

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);

        //login session
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if(session.isLoggedIn()){
            Toast.makeText(this,"Loggedin",Toast.LENGTH_LONG).show();
            navigationView.removeHeaderView(navHeader);
            View view2 = navigationView.inflateHeaderView(R.layout.nav_header_logged);

            TextView nav_name_textView = (TextView)view2.findViewById(R.id.nav_name_textView);
            HashMap<String,String> user = db.getUserDetails();
            String name = "";
            name = user.get("name");

            nav_name_textView.setText(name+" 님");
        }

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

//        loadNavHeader();

//        setNav_hd_name(USER_ID);

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

//    private void openLoginActivity(){
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivityForResult(intent, 9271);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 9271 && resultCode == 3917) {
//            USER_ID = data.getStringExtra("data_id");
//            LOGIN_ID = data.getStringExtra("login_id");
//
//            Constants.LOGIN.LOGIN_NAME = data.getStringExtra("data_id");
//            Constants.LOGIN.LOGIN_ID = data.getStringExtra("login_id");
//
//            Log.e("Intent test", USER_ID);
//            setNav_hd_name(USER_ID);
//
////            setToolbarTitle();
//        }
//    }

//    private void setNav_hd_name(String name){
//        View nav_header_view = navigationView.getHeaderView(0);
//        nav_hd_name = (TextView) nav_header_view.findViewById(R.id.nav_hd_name);
//        nav_hd_name.setText(name + " 님");
//    }

//    private void loadNavHeader() {
//        // showing dot next to notifications label
//        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
//    }

    public void loadHomeFragment() {

        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {

            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                ForecastFragment forecastFragment = new ForecastFragment();
                return forecastFragment;

            case 2:
                MapFragment mapFragment = new MapFragment();
                return mapFragment;

            case 3:
                PortableFragment portableFragment = new PortableFragment();
                return portableFragment;

            case 4:
                GraphFragment graphFragment = new GraphFragment();
                return graphFragment;

            case 5:
                BeaconSrcFragment beaconSrcFragment = new BeaconSrcFragment();
                return beaconSrcFragment;

            case 6:
                SettingFragment settingFragment = new SettingFragment();
                return settingFragment;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
//        toolbar.setTitle(activityTitles[navItemIndex]);

    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_forecast:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_OPT_1;
                        break;
                    case R.id.nav_map:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_OPT_2;
                        break;
                    case R.id.nav_portable:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_OPT_3;
                        break;
                    case R.id.nav_graph:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_OPT_4;
                        break;
                    case R.id.nav_beaconsrc:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_OPT_5;
                        break;
                    case R.id.nav_setting:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_OPT_6;
                        break;
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };


        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

//    @Override
//    public void onBackPressed() {
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//
//            if(CURRENT_TAG != TAG_HOME){
//                homeButtonClicked(R.id.nav_home);
//            } else {
//
//                long tempTime = System.currentTimeMillis();
//                long intervalTime = tempTime - backpressedTime;
//
//                if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
//                    LOGINSTATE = Constants.LOGIN.LOGINFAIL;
//                    super.onBackPressed();
//                } else {
//                    backpressedTime = tempTime;
//                    Toast.makeText(getApplicationContext(), "한번 더 뒤로가기를 누르시면 \n 종료됩니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

//    public void homeButtonClicked(int btn_id){
//        switch (btn_id) {
//
//            case R.id.nav_home:
//                navItemIndex = 0;
//                CURRENT_TAG = TAG_HOME;
//                break;
//            case R.id.nav_option_1:
//                navItemIndex = 1;
//                CURRENT_TAG = TAG_OPT_1;
//                break;
//            case R.id.nav_option_2:
//                navItemIndex = 2;
//                CURRENT_TAG = TAG_OPT_2;
//                break;
//            case R.id.nav_option_3:
//                navItemIndex = 3;
//                CURRENT_TAG = TAG_OPT_3;
//                break;
//            case R.id.nav_option_4:
//                navItemIndex = 4;
//                CURRENT_TAG = TAG_OPT_4;
//                break;
//            default:
//                navItemIndex = 0;
//        }
//
//        loadHomeFragment();
//    }

    public void nav_login_button_clicked(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void nav_logout_button_clicked(View view){
        session.setLogin(false);
        db.deleteUsers();
        Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void opt2clicked(){
        navItemIndex = 1;
        CURRENT_TAG = TAG_OPT_1;
        loadHomeFragment();
    }

    public static String getLOGIN_ID(){
        return LOGIN_ID;
    }
}