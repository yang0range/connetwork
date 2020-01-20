package com.yang.connetwork

import com.yang.connetwork.network.NetWorkApi
import com.yang.connetwork.network.NetWorkBasicResponse
import org.json.JSONException
import org.json.JSONObject


/**
 * @author yangzc
 * @data 2020/1/19 14:22
 * @desc
 */
class GetDataListApi : NetWorkApi(mURL) {

    override val httpRequestType: Int
        get() = HTTP_REQUEST_TYPE_GET


    override fun parseResponseBase(json: JSONObject): NetWorkBasicResponse? {
        try {
            return GetDataListResponse(json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    inner class GetDataListResponse @Throws(JSONException::class)
    constructor(json: JSONObject) : NetWorkBasicResponse(json) {
        var data: String = json.getJSONObject("data").getJSONArray("datas").toString()

    }

    companion object {
        private val mURL = "2/json"
    }


}
