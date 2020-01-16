package com.yang.connetwork.network

import com.kymjs.rxvolley.client.HttpParams

class NetWorkHttpParams : HttpParams() {

    override fun put(key: String, value: String) {
        var value = value
        if (value == null) {
            value = ""
        }
        //		try {
        //			value = URLEncoder.encode(value, "UTF-8");
        //		} catch (UnsupportedEncodingException e) {
        //			e.printStackTrace();
        //		}
        super.put(key, value)
    }
}
