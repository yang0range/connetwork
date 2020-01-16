package com.yang.connetwork.network

import com.kymjs.rxvolley.client.HttpParams

import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap

abstract class NetWorkServerApi(protected val mRelativeURL: String) {

    val httpRequestType: Int
        get() = HTTP_REQUEST_TYPE_GET

    val systemHeader: Map<String, String>
        get() = ConcurrentHashMap()

    protected abstract val serverUrl: String

    val url: String
        get() = serverUrl + mRelativeURL

    val requestParams: HttpParams
        get() = NetWorkHttpParams()

    val tag: String
        get() = this.javaClass.simpleName

    val socketTimeOut: Int
        get() = DEFAULT_SOCKET_TIME_OUT

    val maxRetries: Int
        get() = DEFAULT_MAX_RETRIES

    val isShouldCache: Boolean
        get() = false

    fun getHttpErrorMsg(code: Int): String {
        return NetWorkRequestClient.httpError.getErrorMessage(code)
    }

    fun interceptError(resp: NetWorkBasicResponse): NetWorkBasicResponse {
        return NetWorkRequestClient.httpError.interceptError(resp)
    }

    fun parseResponseBase(json: JSONObject): NetWorkBasicResponse? {
        try {
            val resp = NetWorkBasicResponse(json)
            return if (resp.mStatus == NetWorkBasicResponse.SUCCESS) {
                this.parseReponse(json)
            } else {
                resp
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }

    }

    @Throws(JSONException::class)
    protected fun parseReponse(json: JSONObject): NetWorkBasicResponse {
        return NetWorkBasicResponse(json)
    }

    companion object {

        val HTTP_REQUEST_TYPE_GET = 1

        val HTTP_REQUEST_TYPE_POST = 2

        /**
         * 网络连接超时时间
         */
        val DEFAULT_SOCKET_TIME_OUT = 60000

        /**
         * 请求超时之后重连次数
         */
        private val DEFAULT_MAX_RETRIES = 0
    }
}
