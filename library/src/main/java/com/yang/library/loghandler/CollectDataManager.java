/**
 * <b>File Name:</b>CollectDataManager.java<br />
 * <b>Description:</b><br />
 *  数据采集
 *  <br />
 *
 * <b>History:
 * 2011-04-8 - lipengjun   created </b>
 * 
 * Copyright (c) 2009 VanceInfo Ltd. <br /> All Rights Reserved.
 * @author lipengjun
 */

package com.yang.library.loghandler;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

class CollectDataManager {
    private static String TAG = "CollectDataManager";

    /**
     * 获取应用相关数据
     * 
     * @param paramContext
     * @return
     */
    /**
     * "appinfo":{appid: XXXXXXYOURAPPIDXXXXXXX, appkey:1234567890123456789012,
     * sdktype :android, // SDK类型 appversion:2.1, //应用版本 channel:Unknown //发布渠道
     * },
     */
    protected static JSONObject getAppInfo(Context paramContext) {
        JSONObject localJSONObject = new JSONObject();
        try {
            // String str2 = getAppkey(paramContext);
            // if (str2 == null) {
            // Log.e(TAG, "No appkey");
            // return null;
            // }
            String appVersionName = "Unknown";
            String appVersionCode = "Unknown";
            try {
                appVersionName = paramContext.getPackageManager().getPackageInfo(
                        paramContext.getPackageName(), 0).versionName;
                appVersionCode = paramContext.getPackageManager().getPackageInfo(
                        paramContext.getPackageName(), 0).versionCode
                        + "";
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // localJSONObject.put("appid", str2);
            // localJSONObject.put("appkey", str2);
            // localJSONObject.put("sdktype", "Android");
            localJSONObject.put("appversion", appVersionName);
            // String str3 = getChannel(paramContext);
            // localJSONObject.put("channel", str3);
        } catch (JSONException localJSONException) {
            return null;
        }
        return ((JSONObject) (JSONObject) localJSONObject);
    }

    /**
     * 获取设备相关数据
     * 
     * @param paramContext
     * @return
     */
    /**
     * "deviceinfo":{imei: 44:A7:CF:57:EA:75, //用户终端标识 devicemodel: meizu_m9,
     * //终端型号 carrier:CMCC, //移动运营商 country:china, //国家 accesstype: Wi-Fi,
     * //接入类型 ostype: Android, //操作系统类型 osversion: 2.2, //操作系统版本 cpuinfo:
     * ARMv7Processor rev 2 (v7l), // CPU信息 resolution: 960*640, //屏幕分辨率
     * latitude:-1, //纬度 longitude:-1, //经度 timezone:8, //时区 language: Unknown }
     */
    protected static JSONObject getDeviceInfo(Context paramContext) {

        JSONObject localJSONObject = new JSONObject();
        try {
            TelephonyManager localTelephonyManager = (TelephonyManager) paramContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (localTelephonyManager == null) {
                Log.w(TAG, "No IMEI.");
                return null;
            }

            try {
                String appVersionName = paramContext.getPackageManager().getPackageInfo(
                        paramContext.getPackageName(), 0).versionName;
                localJSONObject.put("appversion", appVersionName);
            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String str1 = "";
            try {
                if (PhoneInfo.checkPermission(paramContext, "android.permission.READ_PHONE_STATE"))
                    str1 = localTelephonyManager.getDeviceId();

            } catch (Exception localException1) {
            }
            if (TextUtils.isEmpty(str1)) {
                Log.w(TAG, "No IMEI.str = " + str1);
                str1 = getMac(paramContext);

                if (str1 == null) {
                    Log.w(TAG, "Failed to take mac as IMEI.");
                    str1 = "unkonwn";
                }

            }
            Log.i(TAG, " IMEI = " + str1);
            localJSONObject.put("imei", str1);

            localJSONObject.put("devicemodel", Build.MODEL);

            try {
                localJSONObject.put("carrier", localTelephonyManager.getNetworkOperatorName());
            } catch (Exception localException4) {
                localJSONObject.put("carrier", "Unknown");
            }

            Configuration curConfiguration = paramContext.getResources().getConfiguration();
            String currentLanguage = curConfiguration.locale.getCountry();
            localJSONObject.put("language", currentLanguage);
            // Configuration localConfiguration = new Configuration();
            Configuration localConfiguration = curConfiguration;
            System.getConfiguration(paramContext.getContentResolver(), localConfiguration);
            if ((localConfiguration != null) && (localConfiguration.locale != null)) {
                java.lang.System.out.println("localConfiguration");
                localJSONObject.put("country", localConfiguration.locale.getCountry());
                // localJSONObject.put("language",
                // localConfiguration.locale.toString());
                Calendar calendar = Calendar.getInstance(localConfiguration.locale);
                if (calendar != null) {
                    TimeZone mTimeZone = ((Calendar) calendar).getTimeZone();
                    if (mTimeZone != null) {
                        localJSONObject.put("timezone",
                                ((TimeZone) mTimeZone).getRawOffset() / 3600000);
                    } else {
                        localJSONObject.put("timezone", 8);
                    }
                } else {
                    localJSONObject.put("timezone", 8);
                }
            } else {
                localJSONObject.put("country", "Unknown");
                // localJSONObject.put("language", "Unknown");
                localJSONObject.put("timezone", 8);
            }

            try {
                String[] arrayOfString = getAccesstype(paramContext);
                localJSONObject.put("accesstype", arrayOfString[0]);
            } catch (Exception localException3) {
                localJSONObject.put("accesstype", "Unknown");
            }

            localJSONObject.put("ostype", "Android");
            localJSONObject.put("osversion", Build.VERSION.RELEASE);
            String cpuInfoStr = PhoneInfo.getCpuInfo();
            localJSONObject.put("cpuinfo", cpuInfoStr);
            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager mWindowManager = (WindowManager) paramContext
                        .getSystemService("window");

                mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
                // int i2 = (int) (( displayMetrics).widthPixels * (
                // displayMetrics).density);
                // int i3 = (int) (( displayMetrics).heightPixels * (
                // displayMetrics).density);
                int screenWidth = displayMetrics.widthPixels;
                int screenHeight = displayMetrics.heightPixels;
                String str4 = screenWidth + "*" + screenHeight;
                localJSONObject.put("resolution", str4);
            } catch (Exception localException2) {
                localJSONObject.put("resolution", "Unknown");
            }
            StringBuilder sb = new StringBuilder();
            Location localLocation = getLocation(paramContext, sb);
            if (localLocation != null) {
                localJSONObject.put("latitude", String.valueOf(localLocation.getLatitude()));
                localJSONObject.put("longitude", String.valueOf(localLocation.getLongitude()));
                Log.i(TAG, "sb = " + sb.toString());
                try {
                    String country = new String(sb.toString().getBytes(), "utf-8");
                    localJSONObject.put("country", country);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                localJSONObject.put("latitude", -1D);
                localJSONObject.put("longitude", -1D);
                localJSONObject.put("country", "Unknown");
            }

        } catch (JSONException localJSONException) {
            return null;
        } catch (SecurityException localSecurityException) {
            Log.e(TAG, "Failed to get IMEI. Forget to add permission READ_PHONE_STATE? ",
                    localSecurityException);
            return null;
        }
        return localJSONObject;

    }

    public static String getDebugInfosToErrorMessage(Context context) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n \n <Debug Infos>");
        sb.append("\n 系统 版本: " + java.lang.System.getProperty("os.version") + " ("
                + Build.VERSION.INCREMENTAL + ")");
        sb.append("\n 系统 API Level: " + Build.VERSION.SDK);
        sb.append("\n 设备: " + Build.DEVICE);
        sb.append("\n 型号 (产品): " + Build.MODEL + " (" + Build.PRODUCT + ")");
        try {
            String appVersionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
            sb.append("\n AppVersion:" + appVersionName);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            Log.w(TAG, "No IMEI.");
            return sb.toString();
        }
        String imei = "";
        try {
            if (PhoneInfo.checkPermission(context, "android.permission.READ_PHONE_STATE"))
                imei = telephonyManager.getDeviceId();
        } catch (Exception localException1) {
        }
        if (TextUtils.isEmpty(imei)) {
            imei = getMac(context);
            if (TextUtils.isEmpty(imei)) {
                imei = "unkonwn";
            }
        }
        sb.append("\n IMEI:" + imei);
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager mWindowManager = (WindowManager) context.getSystemService("window");

            mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;
            String str4 = screenWidth + "*" + screenHeight;
            sb.append("\n 屏幕分辨率:" + str4);
        } catch (Exception localException2) {
            sb.append("\n 屏幕分辨率:Unknown");
        }
        String cpuInfoStr = PhoneInfo.getCpuInfo();
        sb.append("\n cpu处理器信息:" + cpuInfoStr);
        sb.append("\n android版本:" + Build.VERSION.RELEASE);
        try {
            String[] arrayOfString = getAccesstype(context);
            sb.append("\n 网络连接类型:" + arrayOfString[0]);
        } catch (Exception localException3) {
            sb.append("\n 网络连接类型:Unknown");
        }
        try {
            sb.append("\n 运营商:" + telephonyManager.getNetworkOperatorName());
        } catch (Exception e) {
            sb.append("\n 运营商:Unknown");
        }
        Configuration curConfiguration = context.getResources().getConfiguration();
        String currentLanguage = curConfiguration.locale.getCountry();
        sb.append("\n 语言:" + currentLanguage);
        System.getConfiguration(context.getContentResolver(), curConfiguration);
        if ((curConfiguration != null) && (curConfiguration.locale != null)) {
            sb.append("\n 国家:" + curConfiguration.locale.getCountry());
            Calendar calendar = Calendar.getInstance(curConfiguration.locale);
            if (calendar != null) {
                TimeZone mTimeZone = ((Calendar) calendar).getTimeZone();
                if (mTimeZone != null) {
                    sb.append("\n 时区:" + ((TimeZone) mTimeZone).getRawOffset() / 3600000);
                } else {
                    sb.append("\n 时区:" + 8);
                }
            } else {
                sb.append("\n 时区:" + 8);
            }
        } else {
            sb.append("\n 国家:Unknown");
            sb.append("\n 时区:" + 8);
        }

        StringBuilder strbuilder = new StringBuilder();
        Location localLocation = getLocation(context, strbuilder);
        if (localLocation != null) {
            sb.append("\n 经度(longitude):" + String.valueOf(localLocation.getLongitude()));
            sb.append("\n 纬度(latitude):" + String.valueOf(localLocation.getLatitude()));
            try {
                String country = new String(strbuilder.toString().getBytes(), "utf-8");
                sb.append("\n 国家地理:" + country);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            sb.append("\n 经度(longitude):" + -1D);
            sb.append("\n 纬度(latitude):" + -1D);
        }
        sb.append(" \n ...itotem app error info...");
        return sb.toString();
    }

    /**
     * 创建sessionId 返回
     * 
     * @param mContext2
     * @param appkey
     * @param localSharedPreferences
     * @return
     */
    protected static String createSessionId(String appkey) {
        // TODO Auto-generated method stub
        long l1 = java.lang.System.currentTimeMillis();
        String sessionId = appkey + String.valueOf(l1);
        return sessionId;
    }

    /**
     * 获得当前的坐标信息
     * 
     * @param paramContext
     * @return
     */
    private static Location getLocation(Context paramContext) {
        // LocationInfo localf = new LocationInfo(paramContext);
        // Location localLocation = localf.getLastLocation();
        // String add =
        // localf.updateWithNewLocation(localLocation,paramContext);
        return getLocation(paramContext, null);
    }

    /**
     * 获得当前的坐标信息
     * 
     * @param paramContext
     * @return
     */
    private static Location getLocation(Context paramContext, StringBuilder sb) {
        LocationInfo localf = new LocationInfo(paramContext);
        Location localLocation = localf.getLastLocation();
        String add = localf.updateWithNewLocation(localLocation, paramContext);
        if (sb != null) {
            sb.append(add);
        }
        return localLocation;
    }

    /**
     * 得到接入类型
     * 
     * @param mContext
     */
    protected static String[] getAccesstype(Context mContext) {
        // TODO Auto-generated method stub
        String[] arrayOfString = {
                "Unknown", "Unknown"
        };
        PackageManager localPackageManager = mContext.getPackageManager();
        if (localPackageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE",
                mContext.getPackageName()) != 0) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }
        ConnectivityManager localConnectivityManager = (ConnectivityManager) mContext
                .getSystemService("connectivity");
        if (localConnectivityManager == null) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }
        NetworkInfo localNetworkInfo1 = localConnectivityManager.getNetworkInfo(1);
        if (localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
            arrayOfString[0] = "Wi-Fi";
            return arrayOfString;
        }
        NetworkInfo localNetworkInfo2 = localConnectivityManager.getNetworkInfo(0);
        if (localNetworkInfo2.getState() == NetworkInfo.State.CONNECTED) {
            arrayOfString[0] = "2G/3G";
            arrayOfString[1] = localNetworkInfo2.getSubtypeName();
            return arrayOfString;
        }
        return arrayOfString;
    }

    /**
     * 得到应用发布渠道
     * 
     * @param mContext
     */
    protected static String getChannel(Context mContext) {
        String tempChannel = "Unknown";
        try {
            PackageManager localPackageManager = mContext.getPackageManager();
            ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(
                    mContext.getPackageName(), 128);
            if ((localApplicationInfo != null) && (localApplicationInfo.metaData != null)) {
                String str = localApplicationInfo.metaData.getString("CMCCSTATS _CHANNEL");
                if (str != null)
                    tempChannel = str;
                else
                    Log.i(TAG,
                            "Could not read CMCCSTATS _CHANNEL meta-data from AndroidManifest.xml.");
            }
        } catch (Exception localException) {
            Log.i(TAG, "Could not read CMCCSTATS _CHANNEL meta-data from AndroidManifest.xml.",
                    localException);
        }
        return tempChannel;
    }

    /**
     * 得到应用密钥
     * 
     * @param mContext
     */
    protected static String getAppkey(Context mContext) {
        String localObject = null;
        try {
            PackageManager localPackageManager = mContext.getPackageManager();
            ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(
                    mContext.getPackageName(), 128);
            if (localApplicationInfo != null) {
                String str = localApplicationInfo.metaData.getString("CMCCSTATS _APPKEY");
                if (str != null)
                    localObject = str;
                else
                    Log.i(TAG,
                            "Could not read CMCCSTATS _APPKEY meta-data from AndroidManifest.xml.");
            }
        } catch (Exception localException) {
            Log.i(TAG, "Could not read CMCCSTATS _APPKEY meta-data from AndroidManifest.xml.",
                    localException);
        }
        return localObject;
    }

    /**
     * 得到设备的mac地址
     * 
     * @param mContext
     */
    private static String getMac(Context mContext) {
        // TODO Auto-generated method stub
        String mac = null;
        try {
            WifiManager localWifiManager = (WifiManager) mContext.getSystemService("wifi");
            WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
            mac = localWifiInfo.getMacAddress();
        } catch (Exception localException) {
            Log.i(TAG, "Could not read MAC, forget to include ACCESS_WIFI_STATE permission?",
                    localException);
        }
        return mac;
    }

    /**
     * 获取异常日志信息
     * 
     * @param paramContext
     * @return
     */
    static String getExceptionLog(Context paramContext) {
        String localObject = "";
        try {
            String str1 = paramContext.getPackageName();
            String str2 = "";
            int i1 = 0;
            int i2 = 0;
            ArrayList<String> localArrayList = new ArrayList<String>();
            localArrayList.add("logcat");
            localArrayList.add("-d");
            localArrayList.add("-v");
            localArrayList.add("raw");
            localArrayList.add("-s");
            localArrayList.add("AndroidRuntime:E");
            localArrayList.add("-p");
            localArrayList.add(str1);
            Process localProcess = Runtime.getRuntime().exec(
                    (String[]) localArrayList.toArray(new String[localArrayList.size()]));
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(
                    localProcess.getInputStream()), 1024);
            for (String str3 = localBufferedReader.readLine(); str3 != null; str3 = localBufferedReader
                    .readLine()) {
                if (str3.indexOf("thread attach failed") < 0) {
                    str2 = str2 + str3 + '\n';
                }
                if ((i2 == 0) && (str3.toLowerCase().indexOf("exception") >= 0)) {
                    i2 = 1;
                }
                if ((i1 != 0) || (str3.indexOf(str1) < 0)) {
                    continue;
                }
                i1 = 1;
            }
            if ((str2.length() > 0) && (i2 != 0) && (i1 != 0)) {
                localObject = str2;
            }
            try {
                Runtime.getRuntime().exec("logcat -c");
            } catch (Exception localException2) {
                Log.e(TAG, "Failed to clear log");
            }
        } catch (Exception localException1) {
            Log.e(TAG, "Failed to catch error log");
        }
        return localObject;
    }

}
