package com.yang.connetwork.network

import android.content.Context
import android.text.TextUtils
import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.*
import com.kymjs.rxvolley.http.DefaultRetryPolicy
import com.kymjs.rxvolley.http.RequestQueue
import com.yang.library.loghandler.Log


object NetWorkRequestClient {

    /**
     * tag日志输出
     */
    val TAG = "CeHomeHttp"

    internal var mEnableLogging = false


    lateinit var httpError: NetWorkHttpError
        set

    /**
     * 获取请求序列
     *
     * @return
     */
    val queue: RequestQueue
        get() = RxVolley.getRequestQueue()

    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: Context, httpError: NetWorkHttpError, enableLogging: Boolean) {
        mEnableLogging = enableLogging
        this.httpError = httpError
    }

    @JvmOverloads
    fun execute(
        api: NetWorkServerApi?,
        callback: APIFinishCallback? = null,
        progressListener: ProgressListener? = null
    ) {
        if (api == null) {
            return
        }
        if (api.httpRequestType == NetWorkServerApi.HTTP_REQUEST_TYPE_GET) {
            execute(RxVolley.Method.GET, api, callback, progressListener)
        } else {
            execute(RxVolley.Method.POST, api, callback, progressListener)
        }
    }


    private fun execute(
        method: Int,
        api: NetWorkServerApi,
        callback: APIFinishCallback?,
        progressListener: ProgressListener?
    ) {
        if (mEnableLogging) {
            val url = paramsToUrl(api.url, api.requestParams)
            Log.i(
                TAG,
                "tag=" + api.tag + "/" + (if (method == RxVolley.Method.GET) "get" else "post") + " request=" + url + "  header=" + api.systemHeader.toString()
            )
        }
        val params = api.requestParams
        val headers = api.systemHeader
        if (headers != null && !headers.isEmpty()) {
            for (key in headers.keys) {
                params.putHeaders(key, headers[key])
            }
        }
        val builder = RxVolley.Builder()
        builder.url(api.url).httpMethod(method)
        builder.params(params)
        builder.retryPolicy(
            DefaultRetryPolicy(
                api.socketTimeOut, api.maxRetries,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        builder.callback(NetWorkResponseListener(api, callback))
        builder.setTag(api.tag)
        builder.shouldCache(api.isShouldCache)
        if (progressListener != null) {
            builder.progressListener(progressListener)
        }
        builder.doTask()
    }

    fun download(
        storeFilePath: String,
        url: String,
        progressListener: ProgressListener,
        callback: HttpCallback
    ) {
        val config = RequestConfig()
        config.mShouldCache = false
        config.mUrl = url
        config.mCacheTime = 0
        val request = FileRequest(storeFilePath, config, callback)
        request.setOnProgressListener(progressListener)
        RxVolley.Builder().setRequest(request).doTask()
    }

    /**
     * 取消网络请求
     *
     * @param tag
     */
    fun cancelRequest(tag: String) {
        if (!TextUtils.isEmpty(tag)) {
            RxVolley.getRequestQueue().cancelAll(tag)
        }
    }

    fun clearAllCache() {
        RxVolley.getRequestQueue().cache.clear()
    }

    private fun paramsToUrl(url: String, params: HttpParams?): String {
        if (params == null) {
            return url
        }
        val buffer = StringBuffer(url)
        buffer.append(params.urlParams.toString())
        return buffer.toString()
    }

}
