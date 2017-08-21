package com.hyungjun212naver.finedustproject.App;

/**
 * Created by hyung on 2017-04-14.
 */

public class AppConfig {
    public static String IP = "221.148.116.6";
    public static String SERVICE_KEY = "ssW23XVqj7zYinUM1xK3frFatigBeIMaYxmU1T8Av2R1%2FJI7TT%2F556f4nbWLVA4%2FslXiTzGaWK40yfkC%2FMLFXg%3D%3D";
//    public static String SERVICE_KEY = "Pzx5LUDZZTs8Aw41dt474IEqlitcRQ%2BTYW5dCamWML4pyGghh5Vth%2B3VCAMjP07fjJVnPmK%2Bm%2B5SV9ikZiJYwg%3D%3D";
    public static String URL_LOGIN = "http://"+IP+"/AD_Project/login.php";
    public static String URL_REGISTER = "http://"+IP+"/AD_Project/register.php";

    public static String URL_BEACON_LOCATION = "http://"+IP+"/Ad_Project/4.php";
    public static String URL_BEACON_DATA = "http://"+IP+"/Ad_Project/beacon_value.php";
    public static String URL_SEARCH_BEACON_STATION = "http://"+IP+"/Ad_Project/search_beacon_station.php";
    public static String URL_PORTABLE_DATA_INSERT = "http://"+IP+"/sample2.php";
    public static String URL_BEACON_DATA_INSERT = "http://"+IP+"/beacon_sample.php";

    public static String URL_BEACON_ADD_MARKER = "http://"+IP+"/Ad_Project/add_marker.php";

    public static String URL_DUST = "http://"+IP+"/AD_Project/dust_value.php";


    public static boolean BLUETOOTH_PAIRING = false;
    public static boolean BLUETOOTH_BEACON = false;

    public static boolean back_flag = false;
}
