
package com.yang.library.loghandler;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 获取Location对象
 * 
 * @author perry.li
 */
class LocationInfo {
    private LocationManager mLocationManager;

    private Context mContext;

    private static String TAG = "LocationInfo";

    private Location localLocation;

    public LocationInfo(Context paramContext) {
        this.mContext = paramContext;
    }

    public Location getLastLocation() {
        try {
            // Location localLocation;
            this.mLocationManager = ((LocationManager) this.mContext.getSystemService("location"));
            // mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            // 2,1000, locationListener);
            Log.i(TAG,
                    "android.permission.ACCESS_FINE_LOCATION:"
                            + PhoneInfo.checkPermission(this.mContext,
                                    "android.permission.ACCESS_FINE_LOCATION"));
            // if (PhoneInfo.checkPermission(this.mContext,
            // "android.permission.ACCESS_FINE_LOCATION"))
            // {
            Log.i(TAG, "android.permission.ACCESS_FINE_LOCATION-");
            localLocation = this.mLocationManager.getLastKnownLocation("gps");
            if (localLocation != null) {
                Log.i(TAG, "get location from gps:" + localLocation.getLatitude() + ","
                        + localLocation.getLongitude());
                return localLocation;
            }
            // }
            // if (PhoneInfo.checkPermission(this.mContext,
            // "android.permission.ACCESS_COARSE_LOCATION"))
            // {
            Log.i(TAG, "android.permission.ACCESS_COARSE_LOCATION-");
            localLocation = this.mLocationManager.getLastKnownLocation("network");
            if (localLocation != null) {
                Log.i(TAG, "get location from network:" + localLocation.getLatitude() + ","
                        + localLocation.getLongitude());
                return localLocation;
            }
            // }
            Log.i(TAG,
                    "Could not get location from GPS or Cell-id, lack ACCESS_COARSE_LOCATION or ACCESS_COARSE_LOCATION permission?");
            return null;
        } catch (Exception localException) {
            Log.e(TAG, localException.getMessage());
        }
        return null;
    }

    // private final LocationListener locationListener = new LocationListener()
    // {
    // public void onLocationChanged(Location location) {
    // //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
    // // log it when the location changes
    // if (location != null) {
    // Log.i("SuperMap", "Location changed : Lat: "
    // + location.getLatitude() + " Lng: "
    // + location.getLongitude());
    // }
    // }
    //
    // public void onProviderDisabled(String provider) {
    // // Provider被disable时触发此函数，比如GPS被关闭
    // }
    //
    // public void onProviderEnabled(String provider) {
    // // Provider被enable时触发此函数，比如GPS被打开
    // }
    //
    // public void onStatusChanged(String provider, int status, Bundle extras) {
    // // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
    // }
    // };

    /**
     * @param location 当前坐标
     * @return 地理位置的国家名称
     */
    public String updateWithNewLocation(Location location, Context mContext) {

        String addressString = "Unknown";
        if (location == null) {
            Log.i(TAG, "location is null");
            location = localLocation;
        }
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // 更具地理环境来确定编码
            Geocoder geocoder = new Geocoder(mContext, Locale.CHINA);

            List<Address> allAddress = null;
            try {
                // 取得地址相关的一些信息\经度、纬度
                allAddress = geocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                StringBuilder sbline = new StringBuilder();

                if (allAddress.size() > 0) {
                    Address address = allAddress.get(0);
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sbline.append(address.getAddressLine(i));
                    }
                    addressString = address.getCountryName();
                    if (!addressString.equals("")) {
                        return addressString;
                    }
                    String adminArea = address.getAdminArea();// 省
                    sb.append("adminArea(省)=" + address.getAdminArea()).append("\n");// 省
                    String locality = address.getLocality();
                    sb.append("locality(地区，市)=" + address.getLocality()).append("\n");// 城市名称
                    String featureName = address.getFeatureName();
                    sb.append("FeatureName(街道)=" + address.getFeatureName()).append("\n");// 街道名称，一定要显示

                    addressString = sb.toString();
                    /*
                     * 如果 getMaxAddressLineIndex中只显示“中国“则”str=省+地区+街道 如果程度
                     */
                    int start = sbline.indexOf("中国");
                    String strSbline = sbline.toString();
                    if (strSbline.equals("中国")) {
                        // 不做显示，只显示判断地区和省
                        if (adminArea.equals(locality)) {
                            // 省和地区相同
                            addressString = locality + featureName;// "地区："+"街道："+
                        } else {
                            // 省和地区不一样
                            addressString = adminArea + locality + featureName;// "省："+"地区："+"街道："
                        }
                    } else {
                        if (start != -1) {
                            // 有中国2字
                            String subStr = sbline.toString().substring(start + 2);// 去掉中国2字
                            addressString = subStr + featureName;
                        }
                    }
                    if (locality.contains("市")) {
                        locality = locality.substring(0, locality.indexOf("市"));
                    }
                    // mLocationCurrentCity = locality;// 定位城市赋值
                }
                addressString = sbline.toString();
                return addressString;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "LocationInfo updateWithNewLocation exception : " + e.toString());
            }

        }
        return addressString;
    }

}
