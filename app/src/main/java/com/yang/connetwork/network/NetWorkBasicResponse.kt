package com.yang.connetwork.network

import org.json.JSONException
import org.json.JSONObject

class NetWorkBasicResponse {

    var mStatus: Int = 0

    val mMsg: String

    constructor(status: Int, msg: String) {
        this.mStatus = status
        mMsg = msg
    }

    @Throws(JSONException::class)
    constructor(json: JSONObject) {
        try {
            mStatus = Integer.valueOf(json.get("ret").toString())
        } catch (e: Exception) {
            mStatus = NetWorkHttpError.NETWORK_EXCEPTION
        }

        mMsg = json.optString("msg")
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("status = $mStatus").append(" ")
        sb.append("msg = $mMsg").append(" ")
        return sb.toString()
    }

    companion object {

        val SUCCESS = 0
    }
}
