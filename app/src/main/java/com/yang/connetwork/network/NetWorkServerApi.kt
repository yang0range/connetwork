package com.yang.connetwork.network

import com.kymjs.rxvolley.client.HttpParams

import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap

abstract class NetWorkServerApi(protected val mRelativeURL: String) {

    val httpRequestType: Int
        get() = 1

  open  val systemHeader: Map<String, String>
        get() = ConcurrentHashMap()

    protected abstract val serverUrl: String

    val url: String
        get() = this.serverUrl + this.mRelativeURL

     val requestParams: HttpParams
        get() = NetWorkHttpParams()

    val tag: String
        get() = this.javaClass.simpleName

    val socketTimeOut: Int
        get() = 60000

    val maxRetries: Int
        get() = 0

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
            return if (resp.mStatus == 0) this.parseReponse(json) else resp
        } catch (var3: JSONException) {
            var3.printStackTrace()
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
        val DEFAULT_SOCKET_TIME_OUT = 60000
        private val DEFAULT_MAX_RETRIES = 0
    }
}
