package com.hyungjun212naver.finedustproject.App;

/**
 * Created by hyung on 2017-04-14.
 */

public class AppConfig {
    public static String IP = "192.168.0.5";

    public static String URL_LOGIN = "http://"+IP+"/AD_Project/login.php";
    public static String URL_REGISTER = "http://"+IP+"/AD_Project/register.php";

    public static boolean LOGIN_STATE = false;
    public static String LOGIN_NAME = null;
    public static String LOGIN_EMAIL = null;

    public static boolean BEACON_STATE = false;

    public static boolean BLUETOOTH_PAIRING = false;
    public static boolean BLUETOOTH_BEACON = false;

    public static boolean back_flag = false;

    public static double cLatitude = 37.4829;
    public static double cLongitude = 126.983;


}
