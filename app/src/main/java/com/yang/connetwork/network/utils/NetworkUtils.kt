package com.yang.connetwork.network.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils


object NetworkUtils {
    val NETWORK_TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE
    val NETWORK_TYPE_WIFI = ConnectivityManager.TYPE_WIFI
    val INTERFACE_ETH0 = "eth0"
    val INTERFACE_WLAN0 = "wlan0"
    val INTERFACE_DEFAULT = "default"

    val isWapNetwork: Boolean
        get() = !TextUtils.isEmpty(proxyHost)

    val proxyHost: String?
        @SuppressLint("ObsoleteSdkInt")
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            System.getProperty("http.proxyHost")
        } else {
            android.net.Proxy.getDefaultHost()
        }

    val proxyPort: Int
        @SuppressLint("ObsoleteSdkInt")
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Integer.valueOf(System.getProperty("http.proxyPort")!!)
        } else {
            Integer.valueOf(android.net.Proxy.getDefaultHost())
        }

    fun isNetworkAvaliable(ctx: Context): Boolean {
        val connectivityManager = ctx
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val net = connectivityManager.activeNetworkInfo
        return net != null && net.isAvailable && net.isConnected
    }


    fun getNetworkType(con: Context): Int {
        val cm = con
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netinfo = cm.activeNetworkInfo
        return if (netinfo != null && netinfo.isAvailable) {
            if (netinfo.type == ConnectivityManager.TYPE_WIFI) {
                NETWORK_TYPE_WIFI
            } else {
                NETWORK_TYPE_MOBILE
            }
        } else NETWORK_TYPE_MOBILE
    }



    /**
     * Get utf8 byte array.
     *
     * @param str
     * @return array of NULL if error was found
     */
    fun getUTF8Bytes(str: String): ByteArray? {
        try {
            return str.toByteArray(charset("UTF-8"))
        } catch (ex: Exception) {
            return null
        }

    }



    private fun convertMacAddress(mac: ByteArray?): String? {
        if (mac == null) {
            return null
        }
        val buf = StringBuilder()
        for (idx in mac.indices)
            buf.append(String.format("%02X:", mac[idx]))
        if (buf.length > 0)
            buf.deleteCharAt(buf.length - 1)

        // FIXME 为什么有些android获取不到mac地址，或者获取到的mac地址位数不对
        return if (buf.length != 12 && buf.length != 17) {
            null
        } else buf.toString()
    }


}
