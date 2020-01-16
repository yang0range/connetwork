
package com.yang.library.loghandler;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class PhoneInfo {
    private static String TAG = "PhoneInfo";

    /**
     * 检查清单文件是否配置某些权限
     * 
     * @param paramContext
     * @param paramString
     * @return
     */
    protected static boolean checkPermission(Context paramContext, String paramString) {
        PackageManager localPackageManager = paramContext.getPackageManager();
        return (localPackageManager.checkPermission(paramString, paramContext.getPackageName()) != 0);
    }

    // protected static String[] getGpuInfo(GL10 paramGL10)
    // {
    // try
    // {
    // String[] arrayOfString = new String[2];
    // String str1 = paramGL10.glGetString(7936);
    // String str2 = paramGL10.glGetString(7937);
    // arrayOfString[0] = str1;
    // arrayOfString[1] = str2;
    // return arrayOfString;
    // }
    // catch (Exception localException)
    // {
    // Log.e(TAG, "Could not read gpu infor:", localException);
    // }
    // return new String[0];
    // }

    /**
     * 获取终端设备的CPU信息
     */
    protected static String getCpuInfo() {
        String str = null;
        FileReader localFileReader = null;
        BufferedReader localBufferedReader = null;
        try {
            localFileReader = new FileReader("/proc/cpuinfo");
            if (localFileReader != null)
                try {
                    localBufferedReader = new BufferedReader(localFileReader, 1024);
                    str = localBufferedReader.readLine();
                    localBufferedReader.close();
                    localFileReader.close();
                } catch (IOException localIOException) {
                    Log.e(TAG, "Could not read from file /proc/cpuinfo", localIOException);
                }
        } catch (FileNotFoundException localFileNotFoundException) {
            Log.e(TAG, "Could not open file /proc/cpuinfo", localFileNotFoundException);
        }
        if (str != null) {
            int i = str.indexOf(58) + 1;
            str = str.substring(i);
        }
        return str.trim();
    }
}
