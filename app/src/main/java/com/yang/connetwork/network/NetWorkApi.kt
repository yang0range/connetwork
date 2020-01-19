package com.yang.connetwork.network

import android.os.Build

import com.yang.connetwork.BuildConfig
import com.yang.connetwork.Constants

/**
 * @author yangzc
 * @data 2020/1/16 17:02
 * @desc
 */
open class NetWorkApi(mRelativeURL: String) : NetWorkServerApi(mRelativeURL) {

    override
            /**
             * 调整环境
             */
    val serverUrl: String
        get() = if (BuildConfig.DEBUG) Constants.RELEASE_URL else Constants.TEST_URL


    /**
     * 请求头
     */
    override val systemHeader: Map<String, String>
        get() {
            val map = super.systemHeader.toMutableMap()
            map["client"]="android"
            map["mobilebrand"]=Build.BRAND
            map["mobilemodel"]=Build.MODEL
            return map
        }


}
